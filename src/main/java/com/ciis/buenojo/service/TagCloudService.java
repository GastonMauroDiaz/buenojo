package com.ciis.buenojo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ciis.buenojo.domain.TagPair;
import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.ImageCompletionExercise;
import com.ciis.buenojo.domain.Tag;
import com.ciis.buenojo.domain.TagPool;
import com.ciis.buenojo.domain.enumeration.TagPoolColumn;
import com.ciis.buenojo.repository.ImageCompletionExerciseRepository;
import com.ciis.buenojo.repository.TagPoolRepository;
import com.ciis.buenojo.repository.TagRepository;

@Service
@Transactional
public class TagCloudService {
	private static final Integer TAG_CLOUD_SIZE = 14;
	private final Logger log = LoggerFactory.getLogger(TagCloudService.class);

	@Inject
	TagRepository tagRepository;

	@Inject
	TagPoolRepository tagPoolRepository;
	@Inject
	ImageCompletionExerciseRepository exerciseRepository;
	/**
	 * Creates the tag cloud based on the existing tags on this exercise.
	 * responds to the following criteria:
	 *
	 * <blockquote>
	 *  La nube de etiquetas tendrá 14 etiquetas, entre ellas, las etiquetas de la solución, que son aproximadamente cinco. Para obtener el resto de las etiquetas, el sistema deberá buscar en el Pool-de-etiquetas. Para ello, tomará la primer solución y buscará una coincidencia en la columna “Etiqueta”, localizando de ese modo una fila. Dada la fila, extraerá las etiquetas de las columnas “Etiqueta similar 1”,  “Etiqueta similar 2” y “Etiqueta similar 3. Repitiendo esta operación para cada solución, se obtendrá una tabla de cuatro columnas y tantas filas como soluciones. En resumen, esta tabla será un recorte del Pool-de-etiquetas.
	 *
	 * Para obtener las 14 etiquetas que formarán la Nube-de-etiquetas, el sistema deberá eliminar las etiquetas repetidas del recorte, contar cuántas quedan y, cuando el conteo sea menor a 14, completar con etiquetas tomadas al azar del conjunto que no forman parte del recorte.
	 * En pantalla, la Nube-de-etiquetas deberá presentar sus etiquetas en orden alfabético.
	 * </blockquote>
	 * @param exercise
	 * @return
	 */
	public List<Tag> createTagCloudForExercise(Long exerciseId){
		ImageCompletionExercise exercise = exerciseRepository.findOne(exerciseId);
		HashSet<Tag> tagSet = new HashSet<>(TAG_CLOUD_SIZE);
		log.debug("Creating tag cloud for exercise id"+exercise.getId());
		List<TagPool> tagPools =tagPoolRepository.findByTagInListOrderBySimilarity(exercise.getTags());
		ArrayList<Tag> solutionTags = new ArrayList<>();
		for (TagPair tagPair : exercise.getImageCompletionSolution().getTagPairs()) {
			solutionTags.add(tagPair.getTag());

		}
		tagSet.addAll(solutionTags);
		Integer remainingTags = TAG_CLOUD_SIZE - tagSet.size();
		

		for (int similarity = TagPoolColumn.SIMILAR_1.ordinal(); (remainingTags > 0) && similarity <= TagPoolColumn.SIMILAR_3.ordinal(); similarity++) {
			final Integer targetSimilarity = new Integer(similarity);

			int iterated = 0;
			for (Iterator<TagPool> iterator = tagPools.stream().filter(tp -> tp.getSimilarity().equals(targetSimilarity)).iterator(); remainingTags >0 && iterator.hasNext();) {
				TagPool tagPool = iterator.next();
				Tag similar = tagPool.getSimilarTag();
				if (!tagSet.contains(similar)) {
					tagSet.add(similar);
					iterated++;
				}
			}
			remainingTags -= iterated;

		}

		if (remainingTags >0) {
			Course course = exercise.getCourse();
			List<Tag> restOfTags = tagRepository.findByCourseNotInList(course, new ArrayList<Tag>(tagSet));

			Random random = new Random(System.currentTimeMillis());
			for (int r = remainingTags; r >0 ;r--) {
				int drawIndex = random.nextInt(restOfTags.size());
				Tag t = restOfTags.get(drawIndex);
				restOfTags.remove(drawIndex);
				tagSet.add(t);

			}
		}

		ArrayList<Tag> tagList = new ArrayList<>(tagSet);
		tagList.sort(Tag.nameComparator());

		return tagList;
	}





}
