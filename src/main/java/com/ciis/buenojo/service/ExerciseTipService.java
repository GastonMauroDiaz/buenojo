package com.ciis.buenojo.service;

import java.security.SecureRandom;
import java.security.Security;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.ExerciseTip;
import com.ciis.buenojo.domain.ImageResource;
import com.ciis.buenojo.domain.SatelliteImage;
import com.ciis.buenojo.domain.Tag;
import com.ciis.buenojo.domain.enumeration.Region;
import com.ciis.buenojo.domain.enumeration.SatelliteImageType;
import com.ciis.buenojo.domain.parsers.ImageCompletionTipCSVParser;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.repository.CourseRepository;
import com.ciis.buenojo.repository.ExerciseTipRepository;
import com.ciis.buenojo.repository.ImageResourceRepository;
import com.ciis.buenojo.repository.TagRepository;
import com.ciis.buenojo.repository.SatelliteImageRepository;
@Service
@Transactional
public class ExerciseTipService {

	@Inject
	private ExerciseTipRepository tipRepository;

	@Inject
	private CourseRepository courseRepository;
	@Inject
	private ImageResourceRepository imageRepository;
	@Inject
	private SatelliteImageRepository satelliteImageRepository;
	@Inject
	private TagRepository tagRepository;

	public List<ExerciseTip> tipsFromCSVForCourse (InputStreamSource source, Long courseId) throws BuenOjoCSVParserException{

		Course course = courseRepository.findOne(courseId);


		ImageCompletionTipCSVParser parser = new ImageCompletionTipCSVParser(source, course,tagRepository,imageRepository);

		List<ExerciseTip> tipList = parser.parse();

		tipRepository.save(tipList);

		tipRepository.flush();
		return tipList;
	}

	/**
	 * Si el usuario arrastra y suelta la etiqueta correcta, el sistema deberá buscar en la columna “Etiqueta” de la Bolsa-de-tips todas las coincidencia con dicha etiqueta. Luego, el sistema deberá clasificar el ejercicio en las categorías de la tabla 3 y 4. Para ello, deberá utilizar el metadato “lat” y el prefijo del nombre de los archivos (tabla 1). Con estas categorías, el sistema deberá realizar un recorte de la Bolsa-de-tips (notar que siempre debe incluir a Generales). El tip deberá extraerse aleatoriamente del recorte.
	Si el usuario arrastra y suelta la etiqueta incorrecta, el sistema deberá realizar este procedimiento para la etiqueta seleccionada.
	Es importante que, cuando el tip seleccionado sea espécifico a una imagen, se active automáticamente la pestaña que contiene a la imagen relacionada con el tips que se va a mostrar.
	{@link https://docs.google.com/document/d/1BHBtwGcmYtks0fKvZuIZAeJKvTWbNw2W1F8_DHLWStk}
	 * @param tagId
	 * @param satelliteImageId
	 * @return
	 */
	public Optional<ExerciseTip> tipForTagAndSatelliteImage (Long tagId, Long satelliteImageId) {
		 Tag tag = tagRepository.findOne(tagId);
		 SatelliteImage image = satelliteImageRepository.findOne(satelliteImageId);

		 EnumSet<Region> regions = EnumSet.of(Region.regionFromLatitude(image.getLat()), Region.General);
		 SatelliteImageType imageType = image.getImageType();
		 List<ExerciseTip> tips = tipRepository.findByTagRegionAndSatelliteImageType(tag, regions, EnumSet.of(imageType,SatelliteImageType.General));

		 if (tips.size() == 0) return Optional.empty();
		 
		 Random random = new Random(System.currentTimeMillis());
		 
		 Integer tipIndex =  random.nextInt(tips.size());
		 ExerciseTip tip =tips.get(tipIndex);
		 
		 //PATCH: tips no tienen imágenes
		 if (tip.getImages().size() == 0) {
			 for (ExerciseTip t :tips){
				 if ( t.getImages().size()>0 && (t.getImageTypes().contains(SatelliteImageType.General) || t.getImageTypes().contains(imageType))){
					 Set<ImageResource> s = new HashSet<>(t.getImages());
					 tip.setImages(s);
					 return Optional.of(tip);
				 }
			 }

		 }
		 return Optional.of(tip);

	}
}
