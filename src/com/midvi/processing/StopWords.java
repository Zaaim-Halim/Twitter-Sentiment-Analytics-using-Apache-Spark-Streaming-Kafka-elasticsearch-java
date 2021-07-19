package com.midvi.processing;

import java.util.Hashtable;

public class StopWords {
	private Hashtable<String, Boolean> h;
	private static StopWords instance;
	final String[] morstopWords = { "lol", "nai", "haha", "hahaha", "hahahahah", "tru", "glo", "isnt", "dont", "the",
			"a", "about", "above", "after", "again", "against", "ain", "all", "am", "an", "and", "any", "are", "aren",
			"aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by",
			"can", "couldn", "couldn't", "d", "did", "didn", "didn't", "do", "does", "doesn", "doesn't", "doing", "don",
			"don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn", "hadn't", "has", "hasn",
			"hasn't", "have", "haven", "haven't", "having", "he", "her", "here", "hers", "herself", "him", "himself",
			"his", "how", "i", "if", "in", "into", "is", "isn", "isn't", "it", "it's", "its", "itself", "just", "ll",
			"m", "ma", "me", "mightn", "mightn't", "more", "most", "mustn", "mustn't", "my", "myself", "needn",
			"needn't", "no", "nor", "not", "now", "o", "of", "off", "on", "once", "only", "or", "other", "our", "ours",
			"ourselves", "out", "over", "own", "re", "s", "same", "shan", "shan't", "she", "she's", "should",
			"should've", "shouldn", "shouldn't", "so", "some", "such", "t", "than", "that", "that'll", "the", "their",
			"theirs", "them", "themselves", "then", "there", "these", "they", "this", "those", "through", "to", "too",
			"under", "until", "up", "ve", "very", "was", "wasn", "wasn't", "we", "were", "weren", "weren't", "what",
			"when", "where", "which", "while", "who", "whom", "why", "will", "with", "won", "won't", "wouldn",
			"wouldn't", "y", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves",
			"could", "he'd", "he'll", "he's", "here's", "how's", "i'd", "i'll", "i'm", "i've", "let's", "ought",
			"she'd", "she'll", "that's", "there's", "they'd", "they'll", "they're", "they've", "we'd", "we'll", "we're",
			"we've", "what's", "when's", "where's", "who's", "why's", "would", "able", "abst", "accordance",
			"according", "accordingly", "across", "act", "actually", "added", "adj", "affected", "affecting", "affects",
			"afterwards", "ah", "almost", "alone", "along", "already", "also", "although", "always", "among", "amongst",
			"announce", "another", "anybody", "anyhow", "anymore", "anyone", "anything", "anyway", "anyways",
			"anywhere", "apparently", "approximately", "arent", "arise", "around", "aside", "ask", "asking", "auth",
			"available", "away", "awfully", "b", "back", "became", "become", "becomes", "becoming", "beforehand",
			"begin", "beginning", "beginnings", "begins", "behind", "believe", "beside", "besides", "beyond", "biol",
			"brief", "briefly", "c", "ca", "came", "cannot", "can't", "cause", "causes", "certain", "certainly", "co",
			"com", "come", "comes", "contain", "containing", "contains", "couldnt", "date", "different", "done",
			"downwards", "due", "e", "ed", "edu", "effect", "eg", "eight", "eighty", "either", "else", "elsewhere",
			"end", "ending", "enough", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone",
			"everything", "everywhere", "ex", "except", "f", "far", "ff", "fifth", "first", "five", "fix", "followed",
			"following", "follows", "former", "formerly", "forth", "found", "four", "furthermore", "g", "gave", "get",
			"gets", "getting", "give", "given", "gives", "giving", "go", "goes", "gone", "got", "gotten", "h",
			"happens", "hardly", "hed", "hence", "hereafter", "hereby", "herein", "heres", "hereupon", "hes", "hi",
			"hid", "hither", "home", "howbeit", "however", "hundred", "id", "ie", "im", "immediate", "immediately",
			"importance", "important", "inc", "indeed", "index", "information", "instead", "invention", "inward", "itd",
			"it'll", "j", "k", "keep", "keeps", "kept", "kg", "km", "know", "known", "knows", "l", "largely", "last",
			"lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely",
			"line", "little", "'ll", "look", "looking", "looks", "ltd", "made", "mainly", "make", "makes", "many",
			"may", "maybe", "mean", "means", "meantime", "meanwhile", "merely", "mg", "might", "million", "miss", "ml",
			"moreover", "mostly", "mr", "mrs", "much", "mug", "must", "n", "na", "name", "namely", "nay", "nd", "near",
			"nearly", "necessarily", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next",
			"nine", "ninety", "nobody", "non", "none", "nonetheless", "noone", "normally", "nos", "noted", "nothing",
			"nowhere", "obtain", "obtained", "obviously", "often", "oh", "ok", "okay", "old", "omitted", "one", "ones",
			"onto", "ord", "others", "otherwise", "outside", "overall", "owing", "p", "page", "pages", "part",
			"particular", "particularly", "past", "per", "perhaps", "placed", "please", "plus", "poorly", "possible",
			"possibly", "potentially", "pp", "predominantly", "present", "previously", "primarily", "probably",
			"promptly", "proud", "provides", "put", "q", "que", "quickly", "quite", "qv", "r", "ran", "rather", "rd",
			"readily", "really", "recent", "recently", "ref", "refs", "regarding", "regardless", "regards", "related",
			"relatively", "research", "respectively", "resulted", "resulting", "results", "right", "run", "said", "saw",
			"say", "saying", "says", "sec", "section", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen",
			"self", "selves", "sent", "seven", "several", "shall", "shed", "shes", "show", "showed", "shown", "showns",
			"shows", "significant", "significantly", "similar", "similarly", "since", "six", "slightly", "somebody",
			"somehow", "someone", "somethan", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon",
			"sorry", "specifically", "specified", "specify", "specifying", "still", "stop", "strongly", "sub",
			"substantially", "successfully", "sufficiently", "suggest", "sup", "sure", "take", "taken", "taking",
			"tell", "tends", "th", "thank", "thanks", "thanx", "thats", "that've", "thence", "thereafter", "thereby",
			"thered", "therefore", "therein", "there'll", "thereof", "therere", "theres", "thereto", "thereupon",
			"there've", "theyd", "theyre", "think", "thou", "though", "thoughh", "thousand", "throug", "throughout",
			"thru", "thus", "til", "tip", "together", "took", "toward", "towards", "tried", "tries", "truly", "try",
			"trying", "ts", "twice", "two", "u", "un", "unfortunately", "unless", "unlike", "unlikely", "unto", "upon",
			"ups", "us", "use", "used", "useful", "usefully", "usefulness", "uses", "using", "usually", "v", "value",
			"various", "'ve", "via", "viz", "vol", "vols", "vs", "w", "want", "wants", "wasnt", "way", "wed", "welcome",
			"went", "werent", "whatever", "what'll", "whats", "whence", "whenever", "whereafter", "whereas", "whereby",
			"wherein", "wheres", "whereupon", "wherever", "whether", "whim", "whither", "whod", "whoever", "whole",
			"who'll", "whomever", "whos", "whose", "widely", "willing", "wish", "within", "without", "wont", "words",
			"world", "wouldnt", "www", "x", "yes", "yet", "youd", "youre", "z", "zero", "a's", "ain't", "allow",
			"allows", "apart", "appear", "appreciate", "appropriate", "associated", "best", "better", "c'mon", "c's",
			"cant", "changes", "clearly", "concerning", "consequently", "consider", "considering", "corresponding",
			"course", "currently", "definitely", "described", "despite", "entirely", "exactly", "example", "going",
			"greetings", "hello", "help", "hopefully", "ignored", "inasmuch", "indicate", "indicated", "indicates",
			"inner", "insofar", "it'd", "keep", "keeps", "novel", "presumably", "reasonably", "second", "secondly",
			"sensible", "serious", "seriously", "sure", "t's", "third", "thorough", "thoroughly", "three", "well",
			"wonder", "a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all",
			"almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "amoungst",
			"amount", "an", "and", "another", "any", "anyhow", "anyone", "anything", "anyway", "anywhere", "are",
			"around", "as", "at", "back", "be", "became", "because", "become", "becomes", "becoming", "been", "before",
			"beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both",
			"bottom", "but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de",
			"describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven",
			"else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything",
			"everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former",
			"formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has",
			"hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers",
			"herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed",
			"interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less",
			"ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most",
			"mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless",
			"next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off",
			"often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours",
			"ourselves", "out", "over", "own", "part", "per", "perhaps", "please", "put", "rather", "re", "same", "see",
			"seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since",
			"sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes",
			"somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves",
			"then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they",
			"thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus",
			"to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until",
			"up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence",
			"whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether",
			"which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within",
			"without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the", "a", "b", "c", "d", "e",
			"f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z", "co", "op", "research-articl", "pagecount", "cit", "ibid", "les", "le", "au",
			"que", "est", "pas", "vol", "el", "los", "pp", "u201d", "well-b", "http", "volumtype", "par", "0o", "0s",
			"3a", "3b", "3d", "6b", "6o", "a1", "a2", "a3", "a4", "ab", "ac", "ad", "ae", "af", "ag", "aj", "al", "an",
			"ao", "ap", "ar", "av", "aw", "ax", "ay", "az", "b1", "b2", "b3", "ba", "bc", "bd", "be", "bi", "bj", "bk",
			"bl", "bn", "bp", "br", "bs", "bt", "bu", "bx", "c1", "c2", "c3", "cc", "cd", "ce", "cf", "cg", "ch", "ci",
			"cj", "cl", "cm", "cn", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cx", "cy", "cz", "d2", "da", "dc", "dd",
			"de", "df", "di", "dj", "dk", "dl", "do", "dp", "dr", "ds", "dt", "du", "dx", "dy", "e2", "e3", "ea", "ec",
			"ed", "ee", "ef", "ei", "ej", "el", "em", "en", "eo", "ep", "eq", "er", "es", "et", "eu", "ev", "ex", "ey",
			"f2", "fa", "fc", "ff", "fi", "fj", "fl", "fn", "fo", "fr", "fs", "ft", "fu", "fy", "ga", "ge", "gi", "gj",
			"gl", "go", "gr", "gs", "gy", "h2", "h3", "hh", "hi", "hj", "ho", "hr", "hs", "hu", "hy", "i", "i2", "i3",
			"i4", "i6", "i7", "i8", "ia", "ib", "ic", "ie", "ig", "ih", "ii", "ij", "il", "in", "io", "ip", "iq", "ir",
			"iv", "ix", "iy", "iz", "jj", "jr", "js", "jt", "ju", "ke", "kg", "kj", "km", "ko", "l2", "la", "lb", "lc",
			"lf", "lj", "ln", "lo", "lr", "ls", "lt", "m2", "ml", "mn", "mo", "ms", "mt", "mu", "n2", "nc", "nd", "ne",
			"ng", "ni", "nj", "nl", "nn", "nr", "ns", "nt", "ny", "oa", "ob", "oc", "od", "of", "og", "oi", "oj", "ol",
			"om", "on", "oo", "oq", "or", "os", "ot", "ou", "ow", "ox", "oz", "p1", "p2", "p3", "pc", "pd", "pe", "pf",
			"ph", "pi", "pj", "pk", "pl", "pm", "pn", "po", "pq", "pr", "ps", "pt", "pu", "py", "qj", "qu", "r2", "ra",
			"rc", "rd", "rf", "rh", "ri", "rj", "rl", "rm", "rn", "ro", "rq", "rr", "rs", "rt", "ru", "rv", "ry", "s2",
			"sa", "sc", "sd", "se", "sf", "si", "sj", "sl", "sm", "sn", "sp", "sq", "sr", "ss", "st", "sy", "sz", "t1",
			"t2", "t3", "tb", "tc", "td", "te", "tf", "th", "ti", "tj", "tl", "tm", "tn", "tp", "tq", "tr", "ts", "tt",
			"tv", "tx", "ue", "ui", "uj", "uk", "um", "un", "uo", "ur", "ut", "va", "wa", "vd", "wi", "vj", "vo", "wo",
			"vq", "vt", "vu", "x1", "x2", "x3", "xf", "xi", "xj", "xk", "xl", "xn", "xo", "xs", "xt", "xv", "xx", "y2",
			"yj", "yl", "yr", "ys", "yt", "zi", "zz" };

	public StopWords() {
		h = new Hashtable<String, Boolean>();
		h.put("0", true);
		h.put("1", true);
		h.put("2", true);
		h.put("3", true);
		h.put("4", true);
		h.put("5", true);
		h.put("6", true);
		h.put("7", true);
		h.put("8", true);
		h.put("9", true);
		h.put("about", true);
		h.put("above", true);
		h.put("after", true);
		h.put("again", true);
		h.put("against", true);
		h.put("all", true);
		h.put("am", true);
		h.put("an", true);
		h.put("and", true);
		h.put("any", true);
		h.put("are", true);
		h.put("aren't", true);
		h.put("as", true);
		h.put("at", true);
		h.put("be", true);
		h.put("because", true);
		h.put("been", true);
		h.put("before", true);
		h.put("being", true);
		h.put("below", true);
		h.put("between", true);
		h.put("both", true);
		h.put("but", true);
		h.put("by", true);
		h.put("can't", true);
		h.put("cannot", true);
		h.put("could", true);
		h.put("couldn't", true);
		h.put("did", true);
		h.put("didn't", true);
		h.put("do", true);
		h.put("does", true);
		h.put("doesn't", true);
		h.put("doing", true);
		h.put("don't", true);
		h.put("down", true);
		h.put("during", true);
		h.put("each", true);
		h.put("few", true);
		h.put("for", true);
		h.put("from", true);
		h.put("further", true);
		h.put("had", true);
		h.put("hadn't", true);
		h.put("has", true);
		h.put("hasn't", true);
		h.put("have", true);
		h.put("haven't", true);
		h.put("having", true);
		h.put("he", true);
		h.put("he'd", true);
		h.put("he'll", true);
		h.put("he's", true);
		h.put("her", true);
		h.put("here", true);
		h.put("here's", true);
		h.put("hers", true);
		h.put("herself", true);
		h.put("him", true);
		h.put("himself", true);
		h.put("his", true);
		h.put("how", true);
		h.put("how's", true);
		h.put("i", true);
		h.put("i'd", true);
		h.put("i'll", true);
		h.put("i'm", true);
		h.put("i've", true);
		h.put("if", true);
		h.put("in", true);
		h.put("into", true);
		h.put("is", true);
		h.put("isn't", true);
		h.put("it", true);
		h.put("it's", true);
		h.put("its", true);
		h.put("itself", true);
		h.put("let's", true);
		h.put("me", true);
		h.put("more", true);
		h.put("most", true);
		h.put("mustn't", true);
		h.put("my", true);
		h.put("myself", true);
		h.put("no", true);
		h.put("nor", true);
		h.put("not", true);
		h.put("of", true);
		h.put("off", true);
		h.put("on", true);
		h.put("once", true);
		h.put("only", true);
		h.put("or", true);
		h.put("other", true);
		h.put("ought", true);
		h.put("our", true);
		h.put("ours ", true);
		h.put(" ourselves", true);
		h.put("out", true);
		h.put("over", true);
		h.put("own", true);
		h.put("same", true);
		h.put("shan't", true);
		h.put("she", true);
		h.put("she'd", true);
		h.put("she'll", true);
		h.put("she's", true);
		h.put("should", true);
		h.put("shouldn't", true);
		h.put("so", true);
		h.put("some", true);
		h.put("say", true);
		h.put("said", true);
		h.put("such", true);
		h.put("than", true);
		h.put("that", true);
		h.put("that's", true);
		h.put("the", true);
		h.put("their", true);
		h.put("theirs", true);
		h.put("them", true);
		h.put("themselves", true);
		h.put("then", true);
		h.put("there", true);
		h.put("there's", true);
		h.put("these", true);
		h.put("they", true);
		h.put("they'd", true);
		h.put("they'll", true);
		h.put("they're", true);
		h.put("they've", true);
		h.put("this", true);
		h.put("those", true);
		h.put("through", true);
		h.put("to", true);
		h.put("too", true);
		h.put("under", true);
		h.put("until", true);
		h.put("up", true);
		h.put("very", true);
		h.put("was", true);
		h.put("wasn't", true);
		h.put("we", true);
		h.put("we'd", true);
		h.put("we'll", true);
		h.put("we're", true);
		h.put("we've", true);
		h.put("were", true);
		h.put("weren't", true);
		h.put("what", true);
		h.put("what's", true);
		h.put("when", true);
		h.put("when's", true);
		h.put("where", true);
		h.put("where's", true);
		h.put("which", true);
		h.put("while", true);
		h.put("who", true);
		h.put("who's", true);
		h.put("whom", true);
		h.put("why", true);
		h.put("why's", true);
		h.put("with", true);
		h.put("won't", true);
		h.put("would", true);
		h.put("wouldn't", true);
		h.put("you", true);
		h.put("you'd", true);
		h.put("you'll", true);
		h.put("you're", true);
		h.put("you've", true);
		h.put("your", true);
		h.put("yours", true);
		h.put("yourself", true);
		h.put("yourselves ", true);

		h.put("", true);
		h.put("get", true);

		h.put("got", true);
		h.put("must", true);
		h.put("mustn't", true);
		h.put("done", true);
		addMoreStopWord();
	}

	private void addMoreStopWord() {
		for (String s : morstopWords) {
			if (!isStopWord(s.toLowerCase())) {
				h.put(s.toLowerCase(), true);
			}
		}

	}

	public boolean isStopWord(String s) {
		boolean ret = h.get(s) == null ? false : true;
		if (s.length() == 1)
			ret = true;
		return ret;
	}

	public static StopWords getInstance() {
		if (instance == null)
			instance = new StopWords();
		return instance;
	}

}
