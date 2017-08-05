package com.ciis.buenojo.loaders;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.ImageResource;
import com.ciis.buenojo.domain.PhotoLocationBeacon;
import com.ciis.buenojo.domain.PhotoLocationExercise;
import com.ciis.buenojo.domain.PhotoLocationExtraImage;
import com.ciis.buenojo.domain.PhotoLocationExtraSatelliteImage;
import com.ciis.buenojo.domain.PhotoLocationImage;
import com.ciis.buenojo.domain.PhotoLocationKeyword;
import com.ciis.buenojo.domain.PhotoLocationSatelliteImage;
import com.ciis.buenojo.domain.PhotoLocationSightPair;
import com.ciis.buenojo.domain.SatelliteImage;
import com.ciis.buenojo.domain.enumeration.LoaderResult;
import com.ciis.buenojo.domain.enumeration.LoaderType;
import com.ciis.buenojo.domain.enumeration.PhotoLocationDifficulty;
import com.ciis.buenojo.domain.factories.SatelliteImageFactory;
import com.ciis.buenojo.domain.parsers.PhotoLocationBeaconCSVParser;
import com.ciis.buenojo.domain.parsers.PhotoLocationLandscapeLevelsCSVParser;
import com.ciis.buenojo.domain.util.BuenOjoFileUtils;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoDataSetException;
import com.ciis.buenojo.exceptions.BuenOjoDryRunFailedException;
import com.ciis.buenojo.exceptions.BuenOjoDryRunSuccededException;
import com.ciis.buenojo.exceptions.BuenOjoFileException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.exceptions.InvalidSatelliteImageType;
import com.ciis.buenojo.repository.PhotoLocationBeaconRepository;
import com.ciis.buenojo.repository.PhotoLocationExerciseRepository;
import com.ciis.buenojo.repository.PhotoLocationExtraImageRepository;
import com.ciis.buenojo.repository.PhotoLocationExtraSatelliteImageRepository;
import com.ciis.buenojo.repository.PhotoLocationImageRepository;
import com.ciis.buenojo.repository.PhotoLocationKeywordRepository;
import com.ciis.buenojo.repository.PhotoLocationSatelliteImageRepository;
import com.ciis.buenojo.repository.PhotoLocationSightPairRepository;
import com.ciis.buenojo.repository.SatelliteImageRepository;
import com.ciis.buenojo.service.ImageResourceService;
import com.ciis.buenojo.service.PhotoLocationAnnotatedResourceFactory;
import com.ciis.buenojo.service.PhotoLocationKeywordService;
import com.ciis.buenojo.service.PhotoLocationSightPairFactory;
import com.ciis.buenojo.web.rest.dto.ExerciseDataSetDTO;
import com.ciis.buenojo.web.rest.dto.ExerciseDatasetLoaderResultDTO;


@Service

public class PhotoLocationLoader implements DataSetLoader {

	private static final String extraImagesDir = "1024_ImagenesExtra";
	private static final String extraPhotosDir = "FotosExtra";
	private static final String exercisesDir = "Ejercicios";
	private static final String picturesDir = "Fotos";
	private static final String beaconFilename = "indicador.csv";
	private static final String sightFilename = "miras.csv";
	private static final String landscapeLevelsFilename = "niveles_paisaje.csv";
	private static final String keywordsFilename = "keywords.csv";
	private static final String courseKeywordFilename = "keywords_fotolocalizacion.csv";
	private static final String metadataFilename = "metadata.csv";

	private static final int lowerLevel = 0;
	private static final int higherLevel = 1;
	private static final int landscapeLevels = 2;

	@Inject
	private SatelliteImageFactory satelliteImageFactory;

	@Inject
	private ImageResourceService imageResourceService;

	@Inject
	private PhotoLocationAnnotatedResourceFactory annotatedResourceFactory;

	@Inject
	private PhotoLocationExtraSatelliteImageRepository extraSatelliteImagesRepository;

	@Inject
	private PhotoLocationExtraImageRepository extraImageRepository;

	@Inject
	private PhotoLocationBeaconRepository beaconRepository;

	@Inject
	private PhotoLocationKeywordRepository keywordRepository;

	@Inject
	private PhotoLocationKeywordService keywordService;

	@Inject
	private PhotoLocationExerciseRepository exerciseRepository;

	@Inject
	private PhotoLocationSightPairFactory sightPairFactory;

	@Inject
	private PhotoLocationSightPairRepository sightPairRepository;

	@Inject
	private SatelliteImageRepository satelliteImageRepository;
	
	@Inject
	private PhotoLocationImageRepository photoLocationImageRepository;
	
	@Inject
	private PhotoLocationSatelliteImageRepository photoLocationSatelliteImageRepository;
	
	@Override
	public LoaderType loaderType() {
		return LoaderType.PhotoLocation;
	}

	@Override
	public ExerciseDatasetLoaderResultDTO load(ExerciseDataSetDTO dataSet, Course course)
			throws BuenOjoDataSetException {

		try {
			return load(dataSet, course, false);
		} catch (BuenOjoDryRunFailedException | BuenOjoDryRunSuccededException e) {
			throw new BuenOjoDataSetException("SE REGISTRÓ UN DRY RUN CUANDO NO DEBERÍA");
		}
	}

	@Override
	
	public ExerciseDatasetLoaderResultDTO load(ExerciseDataSetDTO dataSet, Course course, Boolean dryRun)
			throws BuenOjoDataSetException, BuenOjoDryRunFailedException, BuenOjoDryRunSuccededException {
		List<PhotoLocationExercise> savedExercises = null;
		try {

			// check if there are photoLocation keywords loaded

			if (keywordRepository.findAllByCourseId(course.getId()).isEmpty()) {
				Path keywordPath = Paths.get(dataSet.getPath(), courseKeywordFilename);

				MultipartFile file = BuenOjoFileUtils.multipartFile(keywordPath);

				List<PhotoLocationKeyword> keywords = keywordService.keywordsFromFile(file, course);

				if (keywords == null) {
					throw new BuenOjoInconsistencyException("NO hay PhotoLocationKeywords cargadas para el curso: \""
							+ course.getName() + "\" y no se pudo cargar el archivo de keywords");
				}

				keywords.forEach((keyword) -> keyword.setCourse(course));

				keywords = keywordRepository.save(keywords);
				if (keywords == null) {
					throw new BuenOjoInconsistencyException(
							"No se pudieron guardar las palabras claves para el curso: \"" + course.getName()
									+ "\" y no se pudo cargar el archivo de keywords");
				}
			}

			// load extra satellite images
			loadExtraSatelliteImages(dataSet, course, dryRun);
			// load exercise pictures
			loadExercisePhotos(dataSet, dryRun);

			// load extra photos
			loadExtraPhotos(dataSet, course, dryRun);

			// load exercises

			Path dataSetPath = Paths.get(dataSet.getPath(), exercisesDir);

			ArrayList<PhotoLocationExercise> exercises = new ArrayList<>();

			for (Path path : Files.walk(dataSetPath, 1, FileVisitOption.FOLLOW_LINKS)
					.filter(p -> !p.getFileName().toString().startsWith(".")).collect(Collectors.toList())) {
				if (!path.equals(dataSetPath)) {
					exercises.add(loadExercise(path, course, dryRun));
				}
			}
			savedExercises = exerciseRepository.save(exercises);
			
			
		} catch (IOException | BuenOjoFileException | BuenOjoInconsistencyException | BuenOjoCSVParserException | InvalidSatelliteImageType e) {
			if (dryRun) {
				throw new BuenOjoDryRunFailedException(e.getMessage());
			} else {
				throw new BuenOjoDataSetException(e.getMessage());
			}
		}
		
		
		ExerciseDatasetLoaderResultDTO result = new ExerciseDatasetLoaderResultDTO(dataSet);
		result.setResult(LoaderResult.Done);
		result.setMessage("Dataset Cargado exitosamente");
		
		if (savedExercises != null && !savedExercises.isEmpty()) {
			ArrayList<Long> ids = new ArrayList<>(savedExercises.size());
			savedExercises.stream().forEach(e -> ids.add(e.getId()));
			result.setLoadedExerciseIds(ids);
		}
		
		if (dryRun) {
			throw new BuenOjoDryRunSuccededException("[DRY RUN]Dataset Cargado exitosamente");
		}
		return result;
	}
	
	private void loadSatelliteImages(PhotoLocationExercise exercise, Path exercisePath, Boolean dryRun) throws IOException, BuenOjoCSVParserException, InvalidSatelliteImageType, BuenOjoFileException, BuenOjoInconsistencyException, BuenOjoDataSetException{
		List<Path> satelliteImagePaths = Files.walk(exercisePath, 1, FileVisitOption.FOLLOW_LINKS).filter(p -> {
			Optional<String> type = BuenOjoFileUtils.contentType(p);
			return type.isPresent() && type.get() != "text/csv";
		}).collect(Collectors.toList());
		
		List<SatelliteImage> satelliteImages = new ArrayList<>();
		for (Path path : satelliteImagePaths) {
			String filename = FilenameUtils.removeExtension(path.getFileName().toString());
			Path csv = path.resolveSibling(filename+".csv");
			SatelliteImage s = satelliteImageFactory.imageFromFile(BuenOjoFileUtils.multipartFile(path), BuenOjoFileUtils.multipartFile(csv), dryRun);
			satelliteImages.add(s);
		}
		satelliteImageRepository.save(satelliteImages);
		
		List<PhotoLocationSatelliteImage> pImages = new ArrayList<>();
		
		for (SatelliteImage satelliteImage : satelliteImages) {
			PhotoLocationSatelliteImage pImg = PhotoLocationSatelliteImage.fromSatelliteImage(satelliteImage);
			pImg.setKeywords(exercise.getLandscapeKeywords());
			pImages.add(pImg);
			
		}
		
		photoLocationSatelliteImageRepository.save(pImages);
		
		exercise.setSatelliteImages(new HashSet<>(pImages));
	}
	
	private PhotoLocationExercise loadExercise(Path exercisePath, Course course, Boolean dryRun)
			throws BuenOjoDataSetException, BuenOjoCSVParserException, IOException, InvalidSatelliteImageType, BuenOjoFileException, BuenOjoInconsistencyException {
		PhotoLocationExercise exercise = new PhotoLocationExercise();
		
		// load beacon

		PhotoLocationBeacon beacon = loadBeacon(exercisePath, dryRun);
		beaconRepository.save(beacon);
		exercise.setBeacon(beacon);

		// load sight pairs
		List<PhotoLocationSightPair> sightPairs = loadSightPairs(exercisePath, exercise, dryRun);
		exercise.setSightPairs(new HashSet<>(sightPairs));
		// load landcape levels
		loadLandscapeLevels(exercisePath, exercise, course, dryRun);

		// load satellite images
		loadSatelliteImages(exercise, exercisePath, dryRun);
		exercise.setCourse(course);
		
		
		parseAndInject(exercise, BuenOjoFileUtils.multipartFile(exercisePath.resolve(metadataFilename)));

		exercise = exerciseRepository.save(exercise);
		beacon.setExercise(exercise);
		beaconRepository.save(beacon);
		sightPairRepository.save(sightPairs);
		return exercise;

	}

	private PhotoLocationBeacon loadBeacon(Path exercisePath, Boolean dryRun) throws BuenOjoDataSetException {

		try {

			Path beaconFilePath = exercisePath.resolve(beaconFilename);
			MultipartFile beaconFile;
			beaconFile = BuenOjoFileUtils.multipartFile(beaconFilePath);
			PhotoLocationBeaconCSVParser beaconParser = new PhotoLocationBeaconCSVParser(beaconFile);

			return beaconParser.parse();

		} catch (IOException | BuenOjoCSVParserException e) {
			throw new BuenOjoDataSetException(e);
		}
	}

	private List<PhotoLocationSightPair> loadSightPairs(Path exercisePath, PhotoLocationExercise exercise,
			Boolean dryRun) throws BuenOjoDataSetException {

		try {
			Path sightPairsFilePath = exercisePath.resolve(sightFilename);
			MultipartFile file = BuenOjoFileUtils.multipartFile(sightPairsFilePath);

			return sightPairFactory.setSightPairs(exercise, file);

		} catch (IOException | BuenOjoCSVParserException e) {
			throw new BuenOjoDataSetException(e);
		}

	}

	private void loadLandscapeLevels(Path exercisePath, PhotoLocationExercise exercise, Course course, Boolean dryRun)
			throws BuenOjoDataSetException {

		try {
			MultipartFile levelsFile;

			Path levelsFilePath = exercisePath.resolve(landscapeLevelsFilename);
			levelsFile = BuenOjoFileUtils.multipartFile(levelsFilePath);

			PhotoLocationLandscapeLevelsCSVParser parser = new PhotoLocationLandscapeLevelsCSVParser(levelsFile);
			
	
			List<PhotoLocationKeyword> keywords = keywordRepository.findKeywordWithNames(parser.parseKeywords(), course);
			
			keywords.stream().forEach(k -> k.setCourse(course));

			exercise.setLandscapeKeywords(new HashSet<>(keywords));

			Integer[] levels = parser.parseLevels();

			if (levels == null || levels.length != landscapeLevels) {
				throw new BuenOjoDataSetException("Ocurrió un problema al cargar los niveles del paisaje para la ruta '"
						+ levelsFilePath + "'. el archivo es incorrecto, no tiene niveles o su cantidad es distinta a "
						+ landscapeLevels);
			}
			exercise.setHigherLevel(levels[higherLevel]);
			exercise.setLowerLevel(levels[lowerLevel]);

		} catch (BuenOjoDataSetException | BuenOjoCSVParserException | IOException e) {
			throw new BuenOjoDataSetException(e);
		}

	}
	
	
	private void loadExercisePhotos(ExerciseDataSetDTO dto, Boolean dryRun)
			throws IOException, BuenOjoDataSetException, BuenOjoFileException, BuenOjoInconsistencyException {

		Path picturesPath = Paths.get(dto.getPath(), picturesDir);
		ArrayList<PhotoLocationImage> pImages = new ArrayList<>();
		
		for (Path path : Files.walk(picturesPath, 1, FileVisitOption.FOLLOW_LINKS).collect(Collectors.toList())) {

			Optional<String> contentType = BuenOjoFileUtils.contentType(path);
			if (contentType.isPresent()) {
				ImageResource img = imageResourceService.createImageResource(null, BuenOjoFileUtils.multipartFile(path), dryRun);
				PhotoLocationImage pImg = PhotoLocationImage.fromImageResource(img);
				pImages.add(pImg);
			}
		}
		
		photoLocationImageRepository.save(pImages);
	}

	private void loadExtraPhotos(ExerciseDataSetDTO dataSet, Course course, Boolean dryRun)
			throws BuenOjoDataSetException, IOException {
		try {
			Path photosPath = Paths.get(dataSet.getPath(), extraPhotosDir);
			Path keywordsPath = photosPath.resolve(keywordsFilename);

			// filter anything that is not a csv file
			for (Path path : Files.walk(photosPath, 1, FileVisitOption.FOLLOW_LINKS)
					.filter(p -> !FilenameUtils.getExtension(p.getFileName().toString()).equals("csv"))
					.collect(Collectors.toList())) {
				// is image file
				Optional<String> contentType = BuenOjoFileUtils.contentType(path);
				if (contentType.isPresent()) {

					// save as ImageResource
					MultipartFile file = BuenOjoFileUtils.multipartFile(path);

					imageResourceService.createImageResource(null, file, dryRun);

				}
			}

			MultipartFile keywordsFile = BuenOjoFileUtils.multipartFile(keywordsPath);

			Set<PhotoLocationImage> photos = annotatedResourceFactory.importImageKeywordsFromCSV(keywordsFile, course,
					true);

			List<PhotoLocationExtraImage> extraPhotos = new ArrayList<>(photos.size());
			photos.stream().forEach(p -> {
				PhotoLocationExtraImage xtraImg = new PhotoLocationExtraImage();
				xtraImg.setCourse(course);
				xtraImg.setImage(p);

				extraPhotos.add(xtraImg);

			});

			extraImageRepository.save(extraPhotos);

		} catch (BuenOjoFileException | BuenOjoCSVParserException | BuenOjoInconsistencyException e) {
			throw new BuenOjoDataSetException(e);
		}

	}

	/**
	 * Las imágenes satelitales extra se levantan en base a imágenes satelitales
	 * existentes. Para ello primero tenemos que guardar las imágenes con su
	 * metadata y luego levantarlas como imágenes extra.
	 * 
	 * @param exerciseDTO
	 * @throws BuenOjoDataSetException
	 */
	private void loadExtraSatelliteImages(ExerciseDataSetDTO exerciseDTO, Course course, Boolean dryRun)
			throws BuenOjoDataSetException {

		try {
			Path satelliteImagesPath = Paths.get(exerciseDTO.getPath(), extraImagesDir);
			Map<String, Path> satelliteImagesMap = new HashMap<>();
			Map<String, Path> satelliteCSVMap = new HashMap<>();
			Files.walk(satelliteImagesPath, 1)
					.filter(p -> BuenOjoFileUtils.contentType(p).isPresent()
							&& !FilenameUtils.getExtension(p.getFileName().toString()).equalsIgnoreCase("csv"))
					.forEach(p -> satelliteImagesMap.put(FilenameUtils.removeExtension(p.getFileName().toString()), p));

			Files.walk(satelliteImagesPath, 1)
					.filter(p -> FilenameUtils.getExtension(p.getFileName().toString()).equalsIgnoreCase("csv"))
					.forEach(p -> satelliteCSVMap.put(FilenameUtils.removeExtension(p.getFileName().toString()), p));

			List<SatelliteImage> images = new ArrayList<SatelliteImage>();
			for (String key : satelliteImagesMap.keySet()) {

				MultipartFile csv = BuenOjoFileUtils.multipartFile(satelliteCSVMap.get(key));
				MultipartFile image = BuenOjoFileUtils.multipartFile(satelliteImagesMap.get(key));
				images.add(satelliteImageFactory.imageFromFile(image, csv, dryRun));

			}

			satelliteImageRepository.save(images);

			Path keywordsFilePath = satelliteImagesPath.resolve(keywordsFilename);
			Set<PhotoLocationSatelliteImage> satelliteImages = annotatedResourceFactory
					.importSatelliteImageKeywordsFromCSV(BuenOjoFileUtils.multipartFile(keywordsFilePath), course);

			List<PhotoLocationExtraSatelliteImage> extraSatelliteImages = satelliteImages.stream().map(sImg -> {

				PhotoLocationExtraSatelliteImage xtraImg = new PhotoLocationExtraSatelliteImage();
				xtraImg.setCourse(course);
				xtraImg.setImage(sImg);
				return xtraImg;
			}).collect(Collectors.toList());

			extraSatelliteImagesRepository.save(extraSatelliteImages);
		} catch (IOException | BuenOjoCSVParserException | InvalidSatelliteImageType | BuenOjoFileException
				| BuenOjoInconsistencyException e) {
			throw new BuenOjoDataSetException(e);

		}

	}
	
	private enum MetadataColumns{
		name,
		description,
		difficulty,
		seconds, 
		totalScore,
		imageName
	}
	
	public void parseAndInject(PhotoLocationExercise exercise, InputStreamSource inputStreamSource) throws BuenOjoCSVParserException, IOException{
		CSVParser parser =  CSVFormat.RFC4180.withHeader().withDelimiter(',').withAllowMissingColumnNames(true).parse(new InputStreamReader(inputStreamSource.getInputStream()));
		List<CSVRecord> records = parser.getRecords();
		if (records.size() > 1) {
			throw new BuenOjoCSVParserException("El archivo contiene más de un ejercicio");
		}
		
		if (records.size() == 0) {
			throw new BuenOjoCSVParserException("El archivo de ejericio es inválido");
		}
		
		CSVRecord record = records.get(0);
		String name = record.get(MetadataColumns.name);
		String description = record.get(MetadataColumns.description);
		String difficulty = record.get(MetadataColumns.difficulty);
		String seconds = record.get(MetadataColumns.seconds);
		String totalScore = record.get(MetadataColumns.totalScore.ordinal());
		String imageName  = record.get(MetadataColumns.imageName.ordinal());
		
		exercise.setDescription(description);
		exercise.setName(name);
		exercise.setDifficulty(difficultyFromString(difficulty));
		exercise.setTotalTimeInSeconds(new Integer(seconds));
		exercise.setTotalScore(new Float(totalScore));
		exercise.setExtraPhotosCount(3);
		List<PhotoLocationImage> imgs = photoLocationImageRepository.findAll();
		
		Optional<PhotoLocationImage> opt = imgs.stream().filter(p ->  p.getImage().getName().equals(imageName)).collect(Collectors.toList()).stream().findFirst();
		if (!opt.isPresent()){
			throw new BuenOjoCSVParserException("la imagen '"+imageName+"' no existe en la base de datos");
		}
		PhotoLocationImage img = opt.get();
		img.setKeywords(exercise.getLandscapeKeywords());
		photoLocationImageRepository.save(img);
		exercise.setTerrainPhoto(img);
				
	}
	
	private PhotoLocationDifficulty difficultyFromString(String d) throws BuenOjoCSVParserException{
		switch(d){
		case "Beginner":
			return PhotoLocationDifficulty.Beginner;
		case "Intermediate":
			return PhotoLocationDifficulty.Intermediate;
		case "Advanced":
			return PhotoLocationDifficulty.Advanced;
			
		default:
			throw new BuenOjoCSVParserException("La dificultad '"+d+"' no es válida");
		}
	}
	
}
