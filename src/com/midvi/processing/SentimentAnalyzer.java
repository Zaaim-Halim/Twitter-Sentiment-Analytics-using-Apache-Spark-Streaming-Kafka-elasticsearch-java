package com.midvi.processing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.AbstractCache;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.midvi.functions.Analyse;
import com.midvi.functions.CleanTweetContent;
import com.midvi.functions.MapPrediction;
import com.midvi.functions.Preproccess;
import com.midvi.functions.SendSentimentAnalysis;
import com.midvi.parser.Tweet;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import scala.Tuple2;

public class SentimentAnalyzer implements Serializable {

	private static final long serialVersionUID = 1L;
	private final static String MODELE_PATH = "C:\\Users\\X1\\Desktop\\master-S2\\big-data\\spark\\modele";
	private final static String TRAIN_DATASET_PATH = "C:\\Users\\X1\\Desktop\\master-S2\\big-data\\spark\\tweetdataset\\DATASEJSON.json";
	private final static String PREPARED_TRAIN_DATASET_PATH = "C:\\Users\\X1\\Desktop\\master-S2\\big-data\\spark\\sparkSql\\doc2vecCos.txt";
	private static final String Doc2Vic = "C:\\Users\\X1\\Desktop\\master-S2\\big-data\\spark\\sparkSql\\word2VecModel.txt";
	private JavaSparkContext sparkContext;

	public SentimentAnalyzer(JavaSparkContext sparkContext) {
		this.sparkContext = sparkContext;

	}

	public void preprocessingNLP_DATASET() {
		List<data> dataset = new ArrayList<>();
		Gson gson = new Gson();
		try {
			Reader reader = Files.newBufferedReader(Paths.get(TRAIN_DATASET_PATH));
			dataset = gson.fromJson(reader, new TypeToken<List<data>>() {
			}.getType());
			reader.close();
			System.out.println(dataset.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (data d : dataset) {
			String v1 = d.getText().toLowerCase();
			if (v1.contains("http")) {
				int index = v1.indexOf("http");
				v1 = v1.substring(0, index);
			}
			if (v1.trim().length() < 6)
				v1 = "none";
			if (v1.contains("rt ")) {
				int index = v1.indexOf(":");
				if (index != -1)
					v1 = v1.substring(index, v1.length());
			}
			String regExp = "[^\\p{L}\\p{Z}]";

			v1 = v1.replaceAll(regExp, "");
			v1 = v1.trim().replace('ё', ' ').replace('њ', '\'');
			// removing stop words
			StopWords stopWord = StopWords.getInstance();
			String[] temp = v1.split(" ");
			StringBuilder builder = new StringBuilder();
			for (String s : temp) {
				if (!stopWord.isStopWord(s.trim().toLowerCase())) {
					builder.append(" " + s);
				}
			}
			v1 = builder.toString().trim().replace('\'', (char) 32);
			// remove string with lenght <2
			StringBuilder bd = new StringBuilder();
			for (String s : v1.split(" ")) {
				if ((s.length() >= 3)) {
					bd.append(" " + s);
				}
				v1 = bd.toString().trim();
			}
			// System.out.println("---> :"+v1 + "\n");
			d.setText(builder.toString().trim());

		}
		List<String> lines = new ArrayList<>();
		for (data d : dataset) {
			if (d.getText().trim().length() > 2)
				lines.add(d.getText().trim());
		}
		this.saveLinesToFile(lines);
		// transform the dataset to Doc2Vec
		doc2vec(dataset);
	}

	public void saveLinesToFile(List<String> lines) {
		FileWriter writer = null;
		try {
			writer = new FileWriter("C:\\Users\\X1\\Desktop\\master-S2\\big-data\\spark\\sparkSql\\output.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String str : lines) {
			try {
				writer.write(str);
				writer.write("\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doc2vec(List<data> lines) {
		String outputPathcos = "C:\\Users\\X1\\Desktop\\master-S2\\big-data\\spark\\sparkSql\\doc2vecCos.txt";
		// Enter the directory of the text file
		File inputTxt = new File("C:\\Users\\X1\\Desktop\\master-S2\\big-data\\spark\\sparkSql\\output.txt");
		System.out.println("Start loading data ..." + inputTxt.getName());
		// Download Data
		SentenceIterator iter = new LineSentenceIterator(inputTxt);
		// Word cutting operation
		TokenizerFactory token = new DefaultTokenizerFactory();
		// Remove special symbols and case conversion operations
		token.setTokenPreProcessor(new CommonPreprocessor());
		AbstractCache<VocabWord> cache = new AbstractCache<>();
		// Add a document tag, this is generally read from the file, I use numbers here
		// for aspects
		List<String> labelList = new ArrayList<String>();
		for (int i = 0; i < lines.size(); i++) {
			labelList.add("doc" + i);
		}

		// Set document label
		LabelsSource source = new LabelsSource(labelList);
		System.out.println("Train the model ...");
		ParagraphVectors vec = new ParagraphVectors.Builder().minWordFrequency(1).iterations(5).epochs(1).layerSize(100)
				.learningRate(0.025).labelsSource(source).windowSize(5).iterate(iter).trainWordVectors(false)
				.vocabCache(cache).tokenizerFactory(token).sampling(0).build();

		vec.fit();
		String srt = lines.get(0).getText();
		System.out.println("Similar sentences:");
		Collection<String> lst = vec.wordsNearest("doc0", 10);
		int cossim = analyse(srt);
		System.out.println(cossim);
		System.out.println(lst);
		System.out.println("Output document vector ...");

		WordVectorSerializer.writeParagraphVectors(vec, Doc2Vic);

		// Get the vector corresponding to a word
		System.out.println("Vector Acquisition:");
		double[] docVector = vec.getWordVector("vaccine");
		System.out.println(Arrays.toString(docVector));
		try {
			writeDocVectors(vec, lines, outputPathcos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int analyse(String tweet) {

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = pipeline.process(tweet);
		for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
			return RNNCoreAnnotations.getPredictedClass(tree);
		}
		return 0;
	}

	public double[] getInferenceVec(ParagraphVectors paragraphVectors, String sentence) {
		return paragraphVectors.getWordVectors(Arrays.asList(sentence.split(" "))).toDoubleVector();
	}

	public ParagraphVectors loadDoc2VecModel() {
		try {
			return WordVectorSerializer.readParagraphVectors(Doc2Vic);
		} catch (IOException e) {
			System.out.println("Failed to lead Doc2Vec  model ......");
			e.printStackTrace();
		}
		return null;
	}

	public void writeDocVectors(ParagraphVectors vectors, List<data> lines, String outpath) throws IOException {
		// write operation
		BufferedWriter bufferedWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(outpath)), "gbk"));

		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).getText().trim().length() < 2)
				continue;
			StringBuilder builder = new StringBuilder();
			INDArray vector = vectors.inferVector(lines.get(i).getText());
			double[] doublevec = vector.toDoubleVector();

			for (int j = 0; j < doublevec.length; j++) {
				builder.append(doublevec[j]);

				if (j < vector.length() - 1) {
					builder.append(" ");
				}
			}

			bufferedWriter.write(lines.get(i).getSentiment_Class() + "  " + builder.append("\n").toString());

		}
		bufferedWriter.close();

	}

	public List<Tuple2<Double, java.util.Vector<Double>>> readDataset() {
		List<Tuple2<Double, java.util.Vector<Double>>> labelFeatures = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(PREPARED_TRAIN_DATASET_PATH))) {
			List<String> stringLine = new ArrayList<>();
			String line;
			java.util.Vector<Double> vector;
			while ((line = br.readLine()) != null) {
				vector = new java.util.Vector<Double>();
				stringLine = Arrays.asList(line.trim().split(" "));
				for (int i = 1; i < stringLine.size() - 1; i++) {
					if (!stringLine.get(i).isEmpty())
						vector.add(Double.parseDouble(stringLine.get(i)));
				}
				Tuple2<Double, java.util.Vector<Double>> tuple = new Tuple2<Double, java.util.Vector<Double>>(
						Double.parseDouble(stringLine.get(0)), vector);
				labelFeatures.add(tuple);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return labelFeatures;
	}

	public JavaRDD<LabeledPoint> datasetToRDD() {
		List<Tuple2<Double, java.util.Vector<Double>>> labelFeatures = readDataset();
		List<LabeledPoint> labeledPointList = new ArrayList<>();
		for (Tuple2<Double, java.util.Vector<Double>> tuple : labelFeatures) {
			double[] d = new double[tuple._2().size()];
			for (int i = 0; i < tuple._2().size(); i++) {
				d[i] = Double.valueOf(tuple._2().get(i));
			}
			labeledPointList.add(new LabeledPoint(tuple._1(), new org.apache.spark.mllib.linalg.DenseVector(d)));
		}
		JavaRDD<LabeledPoint> dataset = sparkContext.parallelize(labeledPointList);
		return dataset;
	}

	public static String processtweet(String str) {
		String v1 = str.toLowerCase();
		if (v1.contains("http")) {
			int index = v1.indexOf("http");
			v1 = v1.substring(0, index);
		}
		if (v1.trim().length() < 6)
			v1 = "none";
		if (v1.contains("rt ")) {
			int index = v1.indexOf(":");
			if (index != -1)
				v1 = v1.substring(index, v1.length());
		}
		String regExp = "[^\\p{L}\\p{Z}]";

		v1 = v1.replaceAll(regExp, "");
		v1 = v1.trim().replace('ё', ' ').replace('њ', '\'');
		// removing stop words
		StopWords stopWord = StopWords.getInstance();
		String[] temp = v1.split(" ");
		StringBuilder builder = new StringBuilder();
		for (String s : temp) {
			if (!stopWord.isStopWord(s.trim().toLowerCase())) {
				builder.append(" " + s);
			}
		}
		v1 = builder.toString().trim().replace('\'', (char) 32);
		// remove string with lenght <2
		StringBuilder bd = new StringBuilder();
		for (String s : v1.split(" ")) {
			if ((s.length() >= 3)) {
				bd.append(" " + s);
			}
			v1 = bd.toString().trim();
		}
		return v1;
	}

	public JavaRDD<String> preprocessingNLP_ONLINE(JavaRDD<String> tweetContentRDD) {
		return tweetContentRDD.map(new Preproccess());

	}

	public void trainLogisticRegression() {
		System.out.println(" staterded .......");
		JavaRDD<LabeledPoint> data = datasetToRDD();

		// JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sparkContext.sc(),
		// PREPARED_TRAIN_DATASET_PATH).toJavaRDD();
		System.out.println(" done .......");
		// Split initial RDD into two... [60% training data, 40% testing data].

		JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[] { 0.6, 0.4 }, 11L);
		JavaRDD<LabeledPoint> training = splits[0].cache();
		JavaRDD<LabeledPoint> test = splits[1];

		// Run training algorithm to build the model.
		LogisticRegressionModel model = new LogisticRegressionWithLBFGS().run(training.rdd());
		JavaPairRDD<Object, Object> predictionAndLabels = test
				.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));

		// Get evaluation metrics.
		MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels.rdd());
		double accuracy = metrics.accuracy();
		System.out.println("Accuracy = " + accuracy);

		// Save and load model

		model.save(sparkContext.sc(), MODELE_PATH);

	}

	public void predict(JavaDStream<Tweet> tweetsDStream) {
		JavaDStream<String> contentDStream = tweetsDStream.transform(new CleanTweetContent());

		JavaDStream<String> preprocessedContentdStream = contentDStream
				.transform(new Function<JavaRDD<String>, JavaRDD<String>>() {

					private static final long serialVersionUID = 1L;

					@Override
					public JavaRDD<String> call(JavaRDD<String> v1) throws Exception {
						//return preprocessingNLP_ONLINE(v1);
						return v1;
					}
				});
		JavaDStream<Integer> predictionDstream = preprocessedContentdStream
				.transform(new Function<JavaRDD<String>, JavaRDD<Integer>>() {

					private static final long serialVersionUID = 1L;

					@Override
					public JavaRDD<Integer> call(JavaRDD<String> v1) throws Exception {
						return v1.map(new Analyse());
					}

				});

		// reduce the prediction
		JavaPairDStream<Integer, String> countAndLabelPrediction = predictionDstream.map(new MapPrediction())
				.mapToPair(label -> new Tuple2<String, Integer>(label, 1))
				.reduceByKeyAndWindow((integer1, integer2) -> (integer1 + integer2), Durations.seconds(5000)) // 15 min
				.mapToPair(tuple -> tuple.swap());
		// show the pairPredictionStream just for the sake of sanity
		countAndLabelPrediction.foreachRDD(new VoidFunction<JavaPairRDD<Integer, String>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void call(JavaPairRDD<Integer, String> t) throws Exception {
				t.foreach(new SendSentimentAnalysis());
			}
		});
	}

	public void trainSVM() {
		JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sparkContext.sc(), PREPARED_TRAIN_DATASET_PATH).toJavaRDD();

		// Split initial RDD into two... [60% training data, 40% testing data].
		JavaRDD<LabeledPoint> training = data.sample(false, 0.6, 11L);
		training.cache();
		JavaRDD<LabeledPoint> test = data.subtract(training);

		// Run training algorithm to build the model.
		int numIterations = 100;
		SVMModel model = SVMWithSGD.train(training.rdd(), numIterations);

		// Clear the default threshold.
		model.clearThreshold();

		// Compute raw scores on the test set.
		JavaRDD<Tuple2<Object, Object>> scoreAndLabels = test
				.map(p -> new Tuple2<>(model.predict(p.features()), p.label()));

		// Get evaluation metrics.
		BinaryClassificationMetrics metrics = new BinaryClassificationMetrics(JavaRDD.toRDD(scoreAndLabels));
		double auROC = metrics.areaUnderROC();

		System.out.println("Area under ROC = " + auROC);

		// Save and load model
		saveSVMModel(model);
	}

	public void predictSVM(JavaDStream<Tweet> tweetsDStream) {
		loadSVMModel();
		JavaDStream<Double> predictionStream = tweetsDStream.transform(new Function<JavaRDD<Tweet>, JavaRDD<Double>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public JavaRDD<Double> call(JavaRDD<Tweet> t) throws Exception {
				t.map(new Function<Tweet, String>() {

					private static final long serialVersionUID = 1L;

					@Override
					public String call(Tweet v1) throws Exception {

						return v1.getContent();
					}
				});

				// JavaRDD<Vector> processedTweetContentRDD =
				// preprocessingNLP_ONLINE(tweetContenRDD);
				return null;// model.predict(processedTweetContentRDD);
			}
		});

		// reduce the prediction
		JavaPairDStream<Integer, String> countAndLabelPrediction = predictionStream.map(new Function<Double, String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String call(Double v1) throws Exception {
				System.out.println("PREDICTION :" + v1);
				if (v1.equals(Double.parseDouble("0")))
					return "neutral";
				else if (v1.equals(Double.parseDouble("1.0")))
					return "positive";
				else if (v1.equals(Double.parseDouble("-1.0")))
					return "negative";

				else
					return "neautral";
			}
		}).mapToPair(label -> new Tuple2<String, Integer>(label, 1))
				.reduceByKeyAndWindow((integer1, integer2) -> (integer1 + integer2), Durations.seconds(3600)) // 15 min
				.mapToPair(tuple -> tuple.swap());
		// show the pairPredictionStream just for the sake of sanity
		countAndLabelPrediction.foreachRDD(new VoidFunction<JavaPairRDD<Integer, String>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void call(JavaPairRDD<Integer, String> t) throws Exception {
				t.foreach(new VoidFunction<Tuple2<Integer, String>>() {

					private static final long serialVersionUID = 1L;

					@Override
					public void call(Tuple2<Integer, String> t) throws Exception {
						// System.out.println(String.format("%s, (%d classification)", t._2(), t._1()) +
						// "\n");

					}
				});
			}
		});
	}

	// helpers
	public void saveSVMModel(SVMModel model) {
		model.save(this.sparkContext.sc(), MODELE_PATH);
	}

	public SVMModel loadSVMModel() {
		return SVMModel.load(this.sparkContext.sc(), MODELE_PATH);
	}
}
