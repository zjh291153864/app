package com.zhonghong.xqshijie.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 *   操作文件 的工具类
 */
public class FileUtils {

	public static final String ROOT_DIR = "AppCartoon";
	public static final String DOWNLOAD_DIR = "download";
	public static final String CACHE_DIR = "cache";
	public static final String ICON_DIR = "pics";
	private static HashMap<String, String> openTypeMap = new HashMap<String, String>();

	/** 判断SD卡是否挂载 */
	public static boolean isSDCardAvailable() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/** 获取下载目录 */
	public static String getDownloadDir(Context context) {
		return getDir(DOWNLOAD_DIR,context);
	}

	/** 获取缓存目录 */
	public static String getCacheDir(Context context) {
		return getDir(CACHE_DIR, context);
	}

	/** 获取icon目录 */
	public static String getIconDir(Context context) {
		return getDir(ICON_DIR,context);
	}

	/** 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录 */
	public static String getDir(String name,Context context) {
		StringBuilder sb = new StringBuilder();
		if (isSDCardAvailable()) {
			sb.append(getExternalStoragePath());
		} else {
			sb.append(getCachePath(context));
		}
		sb.append(name);
		sb.append(File.separator);
		String path = sb.toString();
		createDirs(path);
		return path;
	}

	/** 获取SD下的应用目录 */
	public static String getExternalStoragePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		sb.append(File.separator);
		sb.append(ROOT_DIR);
		sb.append(File.separator);
		return sb.toString();
	}

	/** 获取应用的cache目录 */
	public static String getCachePath( Context context) {
		File f =context.getCacheDir();
		if (null == f) {
			return null;
		} else {
			return f.getAbsolutePath() + "/";
		}
	}

	/** 创建文件夹 */
	public static void createDirs(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
	}

	/**
	 * 根据Http头的Content-Type获取网页的编码类型，如果没有设的话则返回null
	 */
	public static String getEncodeType(String contentType) {
		if (!TextUtils.isEmpty(contentType)) {
			int position = contentType.indexOf("charset=");
			if (position >= 0)
				return contentType.substring(position + 8).trim();
		}
		return null;
	}

	public static String getFileName(String contentDisposition, String contentType) {
		char[] nameChar = new char[contentDisposition.length()];
		byte[] nameBytes = new byte[contentDisposition.length()];
		contentDisposition.getChars(0, contentDisposition.length(), nameChar, 0);
		for (int i = 0; i < nameChar.length; i++) {
			nameBytes[i] = (byte) nameChar[i];
		}
		String filename;
		try {
			filename = new String(nameBytes, getEncodeType(contentType));
			if (!TextUtils.isEmpty(filename)) {
				int position = filename.indexOf("filename=");
				if (position > 0)
					return filename.substring(position + 10, filename.length() - 1).trim();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void writeToSDCard(String directory,String fileName, InputStream input) {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File file = new File(directory, fileName);
			boolean isExist = false;
			try {
				FileOutputStream fos = new FileOutputStream(file);
				byte[] b = new byte[2048];
				int j = 0;
				while ((j = input.read(b)) != -1) {
					fos.write(b, 0, j);
				}
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Log.i("tag", "NO SDCard.");
		}
	}

	/**
	 * 通过文件类型打开文件
	 * @param filePath
	 * @return
     */
	public static Intent openFile(String filePath) {
		putType();
		File file = new File(filePath);

		if ((file == null) || !file.exists() || file.isDirectory())
			return null;

		/* 取得扩展名 */
		String end = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length()).toLowerCase();
		return getTypeAndReturnIntent(filePath, end);
	}

	private static Intent getTypeAndReturnIntent(String param, String end) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, openTypeMap.get(end));
		return intent;
	}

	private static void putType() {
		openTypeMap.put(".3gp", "video/3gpp");
		openTypeMap.put(".3gpp", "video/3gpp");
		openTypeMap.put(".aac", "audio/x-mpeg");
		openTypeMap.put(".amr", "audio/x-mpeg");
		openTypeMap.put(".apk", "application/vnd.android.package-archive");
		openTypeMap.put(".avi", "video/x-msvideo");
		openTypeMap.put(".aab", "application/x-authoware-bin");
		openTypeMap.put(".aam", "application/x-authoware-map");
		openTypeMap.put(".aas", "application/x-authoware-seg");
		openTypeMap.put(".ai", "application/postscript");
		openTypeMap.put(".aif", "audio/x-aiff");
		openTypeMap.put(".aifc", "audio/x-aiff");
		openTypeMap.put(".aiff", "audio/x-aiff");
		openTypeMap.put(".als", "audio/x-alpha5");
		openTypeMap.put(".amc", "application/x-mpeg");
		openTypeMap.put(".ani", "application/octet-stream");
		openTypeMap.put(".asc", "text/plain");
		openTypeMap.put(".asd", "application/astound");
		openTypeMap.put(".asf", "video/x-ms-asf");
		openTypeMap.put(".asn", "application/astound");
		openTypeMap.put(".asp", "application/x-asap");
		openTypeMap.put(".asx", " video/x-ms-asf");
		openTypeMap.put(".au", "audio/basic");
		openTypeMap.put(".avb", "application/octet-stream");
		openTypeMap.put(".awb", "audio/amr-wb");
		openTypeMap.put(".bcpio", "application/x-bcpio");
		openTypeMap.put(".bld", "application/bld");
		openTypeMap.put(".bld2", "application/bld2");
		openTypeMap.put(".bpk", "application/octet-stream");
		openTypeMap.put(".bz2", "application/x-bzip2");
		openTypeMap.put(".bin", "application/octet-stream");
		openTypeMap.put(".bmp", "image/bmp");
		openTypeMap.put(".c", "text/plain");
		openTypeMap.put(".class", "application/octet-stream");
		openTypeMap.put(".conf", "text/plain");
		openTypeMap.put(".cpp", "text/plain");
		openTypeMap.put(".cal", "image/x-cals");
		openTypeMap.put(".ccn", "application/x-cnc");
		openTypeMap.put(".cco", "application/x-cocoa");
		openTypeMap.put(".cdf", "application/x-netcdf");
		openTypeMap.put(".cgi", "magnus-internal/cgi");
		openTypeMap.put(".chat", "application/x-chat");
		openTypeMap.put(".clp", "application/x-msclip");
		openTypeMap.put(".cmx", "application/x-cmx");
		openTypeMap.put(".co", "application/x-cult3d-object");
		openTypeMap.put(".cod", "image/cis-cod");
		openTypeMap.put(".cpio", "application/x-cpio");
		openTypeMap.put(".cpt", "application/mac-compactpro");
		openTypeMap.put(".crd", "application/x-mscardfile");
		openTypeMap.put(".csh", "application/x-csh");
		openTypeMap.put(".csm", "chemical/x-csml");
		openTypeMap.put(".csml", "chemical/x-csml");
		openTypeMap.put(".css", "text/css");
		openTypeMap.put(".cur", "application/octet-stream");
		openTypeMap.put(".doc", "application/msword");
		openTypeMap.put(".docx", "application/msword");
		openTypeMap.put(".dcm", "x-lml/x-evm");
		openTypeMap.put(".dcr", "application/x-director");
		openTypeMap.put(".dcx", "image/x-dcx");
		openTypeMap.put(".dhtml", "text/html");
		openTypeMap.put(".dir", "application/x-director");
		openTypeMap.put(".dll", "application/octet-stream");
		openTypeMap.put(".dmg", "application/octet-stream");
		openTypeMap.put(".dms", "application/octet-stream");
		openTypeMap.put(".dot", "application/x-dot");
		openTypeMap.put(".dvi", "application/x-dvi");
		openTypeMap.put(".dwf", "drawing/x-dwf");
		openTypeMap.put(".dwg", "application/x-autocad");
		openTypeMap.put(".dxf", "application/x-autocad");
		openTypeMap.put(".dxr", "application/x-director");
		openTypeMap.put(".ebk", "application/x-expandedbook");
		openTypeMap.put(".emb", "chemical/x-embl-dl-nucleotide");
		openTypeMap.put(".embl", "chemical/x-embl-dl-nucleotide");
		openTypeMap.put(".eps", "application/postscript");
		openTypeMap.put(".epub", "application/epub+zip");
		openTypeMap.put(".eri", "image/x-eri");
		openTypeMap.put(".es", "audio/echospeech");
		openTypeMap.put(".esl", "audio/echospeech");
		openTypeMap.put(".etc", "application/x-earthtime");
		openTypeMap.put(".etx", "text/x-setext");
		openTypeMap.put(".evm", "x-lml/x-evm");
		openTypeMap.put(".evy", "application/x-envoy");
		openTypeMap.put(".exe", "application/octet-stream");
		openTypeMap.put(".fh4", "image/x-freehand");
		openTypeMap.put(".fh5", "image/x-freehand");
		openTypeMap.put(".fhc", "image/x-freehand");
		openTypeMap.put(".fif", "image/fif");
		openTypeMap.put(".fm", "application/x-maker");
		openTypeMap.put(".fpx", "image/x-fpx");
		openTypeMap.put(".fvi", "video/isivideo");
		openTypeMap.put(".flv", "video/x-msvideo");
		openTypeMap.put(".gau", "chemical/x-gaussian-input");
		openTypeMap.put(".gca", "application/x-gca-compressed");
		openTypeMap.put(".gdb", "x-lml/x-gdb");
		openTypeMap.put(".gif", "image/gif");
		openTypeMap.put(".gps", "application/x-gps");
		openTypeMap.put(".gtar", "application/x-gtar");
		openTypeMap.put(".gz", "application/x-gzip");
		openTypeMap.put(".gif", "image/gif");
		openTypeMap.put(".gtar", "application/x-gtar");
		openTypeMap.put(".gz", "application/x-gzip");
		openTypeMap.put(".h", "text/plain");
		openTypeMap.put(".hdf", "application/x-hdf");
		openTypeMap.put(".hdm", "text/x-hdml");
		openTypeMap.put(".hdml", "text/x-hdml");
		openTypeMap.put(".htm", "text/html");
		openTypeMap.put(".html", "text/html");
		openTypeMap.put(".hlp", "application/winhlp");
		openTypeMap.put(".hqx", "application/mac-binhex40");
		openTypeMap.put(".hts", "text/html");
		openTypeMap.put(".ice", "x-conference/x-cooltalk");
		openTypeMap.put(".ico", "application/octet-stream");
		openTypeMap.put(".ief", "image/ief");
		openTypeMap.put(".ifm", "image/gif");
		openTypeMap.put(".ifs", "image/ifs");
		openTypeMap.put(".imy", "audio/melody");
		openTypeMap.put(".ins", "application/x-net-install");
		openTypeMap.put(".ips", "application/x-ipscript");
		openTypeMap.put(".ipx", "application/x-ipix");
		openTypeMap.put(".it", "audio/x-mod");
		openTypeMap.put(".itz", "audio/x-mod");
		openTypeMap.put(".ivr", "i-world/i-vrml");
		openTypeMap.put(".j2k", "image/j2k");
		openTypeMap.put(".jad", "text/vnd.sun.j2me.app-descriptor");
		openTypeMap.put(".jam", "application/x-jam");
		openTypeMap.put(".jnlp", "application/x-java-jnlp-file");
		openTypeMap.put(".jpe", "image/jpeg");
		openTypeMap.put(".jpz", "image/jpeg");
		openTypeMap.put(".jwc", "application/jwc");
		openTypeMap.put(".jar", "application/java-archive");
		openTypeMap.put(".java", "text/plain");
		openTypeMap.put(".jpeg", "image/jpeg");
		openTypeMap.put(".jpg", "image/jpeg");
		openTypeMap.put(".js", "application/x-javascript");
		openTypeMap.put(".kjx", "application/x-kjx");
		openTypeMap.put(".lak", "x-lml/x-lak");
		openTypeMap.put(".latex", "application/x-latex");
		openTypeMap.put(".lcc", "application/fastman");
		openTypeMap.put(".lcl", "application/x-digitalloca");
		openTypeMap.put(".lcr", "application/x-digitalloca");
		openTypeMap.put(".lgh", "application/lgh");
		openTypeMap.put(".lha", "application/octet-stream");
		openTypeMap.put(".lml", "x-lml/x-lml");
		openTypeMap.put(".lmlpack", "x-lml/x-lmlpack");
		openTypeMap.put(".log", "text/plain");
		openTypeMap.put(".lsf", "video/x-ms-asf");
		openTypeMap.put(".lsx", "video/x-ms-asf");
		openTypeMap.put(".lzh", "application/x-lzh ");
		openTypeMap.put(".m13", "application/x-msmediaview");
		openTypeMap.put(".m14", "application/x-msmediaview");
		openTypeMap.put(".m15", "audio/x-mod");
		openTypeMap.put(".m3u", "audio/x-mpegurl");
		openTypeMap.put(".m3url", "audio/x-mpegurl");
		openTypeMap.put(".ma1", "audio/ma1");
		openTypeMap.put(".ma2", "audio/ma2");
		openTypeMap.put(".ma3", "audio/ma3");
		openTypeMap.put(".ma5", "audio/ma5");
		openTypeMap.put(".man", "application/x-troff-man");
		openTypeMap.put(".map", "magnus-internal/imagemap");
		openTypeMap.put(".mbd", "application/mbedlet");
		openTypeMap.put(".mct", "application/x-mascot");
		openTypeMap.put(".mdb", "application/x-msaccess");
		openTypeMap.put(".mdz", "audio/x-mod");
		openTypeMap.put(".me", "application/x-troff-me");
		openTypeMap.put(".mel", "text/x-vmel");
		openTypeMap.put(".mi", "application/x-mif");
		openTypeMap.put(".mid", "audio/midi");
		openTypeMap.put(".midi", "audio/midi");
		openTypeMap.put(".m4a", "audio/mp4a-latm");
		openTypeMap.put(".m4b", "audio/mp4a-latm");
		openTypeMap.put(".m4p", "audio/mp4a-latm");
		openTypeMap.put(".m4u", "video/vnd.mpegurl");
		openTypeMap.put(".m4v", "video/x-m4v");
		openTypeMap.put(".mov", "video/quicktime");
		openTypeMap.put(".mp2", "audio/x-mpeg");
		openTypeMap.put(".mp3", "audio/x-mpeg");
		openTypeMap.put(".mp4", "video/mp4");
		openTypeMap.put(".mpc", "application/vnd.mpohun.certificate");
		openTypeMap.put(".mpe", "video/mpeg");
		openTypeMap.put(".mpeg", "video/mpeg");
		openTypeMap.put(".mpg", "video/mpeg");
		openTypeMap.put(".mpg4", "video/mp4");
		openTypeMap.put(".mpga", "audio/mpeg");
		openTypeMap.put(".msg", "application/vnd.ms-outlook");
		openTypeMap.put(".mif", "application/x-mif");
		openTypeMap.put(".mil", "image/x-cals");
		openTypeMap.put(".mio", "audio/x-mio");
		openTypeMap.put(".mmf", "application/x-skt-lbs");
		openTypeMap.put(".mng", "video/x-mng");
		openTypeMap.put(".mny", "application/x-msmoney");
		openTypeMap.put(".moc", "application/x-mocha");
		openTypeMap.put(".mocha", "application/x-mocha");
		openTypeMap.put(".mod", "audio/x-mod");
		openTypeMap.put(".mof", "application/x-yumekara");
		openTypeMap.put(".mol", "chemical/x-mdl-molfile");
		openTypeMap.put(".mop", "chemical/x-mopac-input");
		openTypeMap.put(".movie", "video/x-sgi-movie");
		openTypeMap.put(".mpn", "application/vnd.mophun.application");
		openTypeMap.put(".mpp", "application/vnd.ms-project");
		openTypeMap.put(".mps", "application/x-mapserver");
		openTypeMap.put(".mrl", "text/x-mrml");
		openTypeMap.put(".mrm", "application/x-mrm");
		openTypeMap.put(".ms", "application/x-troff-ms");
		openTypeMap.put(".mts", "application/metastream");
		openTypeMap.put(".mtx", "application/metastream");
		openTypeMap.put(".mtz", "application/metastream");
		openTypeMap.put(".mzv", "application/metastream");
		openTypeMap.put(".nar", "application/zip");
		openTypeMap.put(".nbmp", "image/nbmp");
		openTypeMap.put(".nc", "application/x-netcdf");
		openTypeMap.put(".ndb", "x-lml/x-ndb");
		openTypeMap.put(".ndwn", "application/ndwn");
		openTypeMap.put(".nif", "application/x-nif");
		openTypeMap.put(".nmz", "application/x-scream");
		openTypeMap.put(".nokia-op-logo", "image/vnd.nok-oplogo-color");
		openTypeMap.put(".npx", "application/x-netfpx");
		openTypeMap.put(".nsnd", "audio/nsnd");
		openTypeMap.put(".nva", "application/x-neva1");
		openTypeMap.put(".oda", "application/oda");
		openTypeMap.put(".oom", "application/x-atlasMate-plugin");
		openTypeMap.put(".ogg", "audio/ogg");
		openTypeMap.put(".pac", "audio/x-pac");
		openTypeMap.put(".pae", "audio/x-epac");
		openTypeMap.put(".pan", "application/x-pan");
		openTypeMap.put(".pbm", "image/x-portable-bitmap");
		openTypeMap.put(".pcx", "image/x-pcx");
		openTypeMap.put(".pda", "image/x-pda");
		openTypeMap.put(".pdb", "chemical/x-pdb");
		openTypeMap.put(".pdf", "application/pdf");
		openTypeMap.put(".pfr", "application/font-tdpfr");
		openTypeMap.put(".pgm", "image/x-portable-graymap");
		openTypeMap.put(".pict", "image/x-pict");
		openTypeMap.put(".pm", "application/x-perl");
		openTypeMap.put(".pmd", "application/x-pmd");
		openTypeMap.put(".png", "image/png");
		openTypeMap.put(".pnm", "image/x-portable-anymap");
		openTypeMap.put(".pnz", "image/png");
		openTypeMap.put(".pot", "application/vnd.ms-powerpoint");
		openTypeMap.put(".ppm", "image/x-portable-pixmap");
		openTypeMap.put(".pps", "application/vnd.ms-powerpoint");
		openTypeMap.put(".ppt", "application/vnd.ms-powerpoint");
		openTypeMap.put(".pptx", "application/vnd.ms-powerpoint");
		openTypeMap.put(".pqf", "application/x-cprplayer");
		openTypeMap.put(".pqi", "application/cprplayer");
		openTypeMap.put(".prc", "application/x-prc");
		openTypeMap.put(".proxy", "application/x-ns-proxy-autoconfig");
		openTypeMap.put(".prop", "text/plain");
		openTypeMap.put(".ps", "application/postscript");
		openTypeMap.put(".ptlk", "application/listenup");
		openTypeMap.put(".pub", "application/x-mspublisher");
		openTypeMap.put(".pvx", "video/x-pv-pvx");
		openTypeMap.put(".qcp", "audio/vnd.qcelp");
		openTypeMap.put(".qt", "video/quicktime");
		openTypeMap.put(".qti", "image/x-quicktime");
		openTypeMap.put(".qtif", "image/x-quicktime");
		openTypeMap.put(".r3t", "text/vnd.rn-realtext3d");
		openTypeMap.put(".ra", "audio/x-pn-realaudio");
		openTypeMap.put(".ram", "audio/x-pn-realaudio");
		openTypeMap.put(".ras", "image/x-cmu-raster");
		openTypeMap.put(".rdf", "application/rdf+xml");
		openTypeMap.put(".rf", "image/vnd.rn-realflash");
		openTypeMap.put(".rgb", "image/x-rgb");
		openTypeMap.put(".rlf", "application/x-richlink");
		openTypeMap.put(".rm", "audio/x-pn-realaudio");
		openTypeMap.put(".rmf", "audio/x-rmf");
		openTypeMap.put(".rmm", "audio/x-pn-realaudio");
		openTypeMap.put(".rnx", "application/vnd.rn-realplayer");
		openTypeMap.put(".roff", "application/x-troff");
		openTypeMap.put(".rp", "image/vnd.rn-realpix");
		openTypeMap.put(".rpm", "audio/x-pn-realaudio-plugin");
		openTypeMap.put(".rt", "text/vnd.rn-realtext");
		openTypeMap.put(".rte", "x-lml/x-gps");
		openTypeMap.put(".rtf", "application/rtf");
		openTypeMap.put(".rtg", "application/metastream");
		openTypeMap.put(".rtx", "text/richtext");
		openTypeMap.put(".rv", "video/vnd.rn-realvideo");
		openTypeMap.put(".rwc", "application/x-rogerwilco");
		openTypeMap.put(".rar", "application/x-rar-compressed");
		openTypeMap.put(".rc", "text/plain");
		openTypeMap.put(".rmvb", "audio/x-pn-realaudio");
		openTypeMap.put(".s3m", "audio/x-mod");
		openTypeMap.put(".s3z", "audio/x-mod");
		openTypeMap.put(".sca", "application/x-supercard");
		openTypeMap.put(".scd", "application/x-msschedule");
		openTypeMap.put(".sdf", "application/e-score");
		openTypeMap.put(".sea", "application/x-stuffit");
		openTypeMap.put(".sgm", "text/x-sgml");
		openTypeMap.put(".sgml", "text/x-sgml");
		openTypeMap.put(".shar", "application/x-shar");
		openTypeMap.put(".shtml", "magnus-internal/parsed-html");
		openTypeMap.put(".shw", "application/presentations");
		openTypeMap.put(".si6", "image/si6");
		openTypeMap.put(".si7", "image/vnd.stiwap.sis");
		openTypeMap.put(".si9", "image/vnd.lgtwap.sis");
		openTypeMap.put(".sis", "application/vnd.symbian.install");
		openTypeMap.put(".sit", "application/x-stuffit");
		openTypeMap.put(".skd", "application/x-koan");
		openTypeMap.put(".skm", "application/x-koan");
		openTypeMap.put(".skp", "application/x-koan");
		openTypeMap.put(".skt", "application/x-koan");
		openTypeMap.put(".slc", "application/x-salsa");
		openTypeMap.put(".smd", "audio/x-smd");
		openTypeMap.put(".smi", "application/smil");
		openTypeMap.put(".smil", "application/smil");
		openTypeMap.put(".smp", "application/studiom");
		openTypeMap.put(".smz", "audio/x-smd");
		openTypeMap.put(".sh", "application/x-sh");
		openTypeMap.put(".snd", "audio/basic");
		openTypeMap.put(".spc", "text/x-speech");
		openTypeMap.put(".spl", "application/futuresplash");
		openTypeMap.put(".spr", "application/x-sprite");
		openTypeMap.put(".sprite", "application/x-sprite");
		openTypeMap.put(".sdp", "application/sdp");
		openTypeMap.put(".spt", "application/x-spt");
		openTypeMap.put(".src", "application/x-wais-source");
		openTypeMap.put(".stk", "application/hyperstudio");
		openTypeMap.put(".stm", "audio/x-mod");
		openTypeMap.put(".sv4cpio", "application/x-sv4cpio");
		openTypeMap.put(".sv4crc", "application/x-sv4crc");
		openTypeMap.put(".svf", "image/vnd");
		openTypeMap.put(".svg", "image/svg-xml");
		openTypeMap.put(".svh", "image/svh");
		openTypeMap.put(".svr", "x-world/x-svr");
		openTypeMap.put(".swf", "application/x-shockwave-flash");
		openTypeMap.put(".swfl", "application/x-shockwave-flash");
		openTypeMap.put(".t", "application/x-troff");
		openTypeMap.put(".tad", "application/octet-stream");
		openTypeMap.put(".talk", "text/x-speech");
		openTypeMap.put(".tar", "application/x-tar");
		openTypeMap.put(".taz", "application/x-tar");
		openTypeMap.put(".tbp", "application/x-timbuktu");
		openTypeMap.put(".tbt", "application/x-timbuktu");
		openTypeMap.put(".tcl", "application/x-tcl");
		openTypeMap.put(".tex", "application/x-tex");
		openTypeMap.put(".texi", "application/x-texinfo");
		openTypeMap.put(".texinfo", "application/x-texinfo");
		openTypeMap.put(".tgz", "application/x-tar");
		openTypeMap.put(".thm", "application/vnd.eri.thm");
		openTypeMap.put(".tif", "image/tiff");
		openTypeMap.put(".tiff", "image/tiff");
		openTypeMap.put(".tki", "application/x-tkined");
		openTypeMap.put(".tkined", "application/x-tkined");
		openTypeMap.put(".toc", "application/toc");
		openTypeMap.put(".toy", "image/toy");
		openTypeMap.put(".tr", "application/x-troff");
		openTypeMap.put(".trk", "x-lml/x-gps");
		openTypeMap.put(".trm", "application/x-msterminal");
		openTypeMap.put(".tsi", "audio/tsplayer");
		openTypeMap.put(".tsp", "application/dsptype");
		openTypeMap.put(".tsv", "text/tab-separated-values");
		openTypeMap.put(".ttf", "application/octet-stream");
		openTypeMap.put(".ttz", "application/t-time");
		openTypeMap.put(".txt", "text/plain");
		openTypeMap.put(".ult", "audio/x-mod");
		openTypeMap.put(".ustar", "application/x-ustar");
		openTypeMap.put(".uu", "application/x-uuencode");
		openTypeMap.put(".uue", "application/x-uuencode");
		openTypeMap.put(".vcd", "application/x-cdlink");
		openTypeMap.put(".vcf", "text/x-vcard");
		openTypeMap.put(".vdo", "video/vdo");
		openTypeMap.put(".vib", "audio/vib");
		openTypeMap.put(".viv", "video/vivo");
		openTypeMap.put(".vivo", "video/vivo");
		openTypeMap.put(".vmd", "application/vocaltec-media-desc");
		openTypeMap.put(".vmf", "application/vocaltec-media-file");
		openTypeMap.put(".vmi", "application/x-dreamcast-vms-info");
		openTypeMap.put(".vms", "application/x-dreamcast-vms");
		openTypeMap.put(".vox", "audio/voxware");
		openTypeMap.put(".vqe", "audio/x-twinvq-plugin");
		openTypeMap.put(".vqf", "audio/x-twinvq");
		openTypeMap.put(".vql", "audio/x-twinvq");
		openTypeMap.put(".vre", "x-world/x-vream");
		openTypeMap.put(".vrml", "x-world/x-vrml");
		openTypeMap.put(".vrt", "x-world/x-vrt");
		openTypeMap.put(".vrw", "x-world/x-vream");
		openTypeMap.put(".vts", "workbook/formulaone");
		openTypeMap.put(".wax", "audio/x-ms-wax");
		openTypeMap.put(".wbmp", "image/vnd.wap.wbmp");
		openTypeMap.put(".web", "application/vnd.xara");
		openTypeMap.put(".wav", "audio/x-wav");
		openTypeMap.put(".wma", "audio/x-ms-wma");
		openTypeMap.put(".wmv", "audio/x-ms-wmv");
		openTypeMap.put(".wi", "image/wavelet");
		openTypeMap.put(".wis", "application/x-InstallShield");
		openTypeMap.put(".wm", "video/x-ms-wm");
		openTypeMap.put(".wmd", "application/x-ms-wmd");
		openTypeMap.put(".wmf", "application/x-msmetafile");
		openTypeMap.put(".wml", "text/vnd.wap.wml");
		openTypeMap.put(".wmlc", "application/vnd.wap.wmlc");
		openTypeMap.put(".wmls", "text/vnd.wap.wmlscript");
		openTypeMap.put(".wmlsc", "application/vnd.wap.wmlscriptc");
		openTypeMap.put(".wmlscript", "text/vnd.wap.wmlscript");
		openTypeMap.put(".wmv", "video/x-ms-wmv");
		openTypeMap.put(".wmx", "video/x-ms-wmx");
		openTypeMap.put(".wmz", "application/x-ms-wmz");
		openTypeMap.put(".wpng", "image/x-up-wpng");
		openTypeMap.put(".wps", "application/vnd.ms-works");
		openTypeMap.put(".wpt", "x-lml/x-gps");
		openTypeMap.put(".wri", "application/x-mswrite");
		openTypeMap.put(".wrl", "x-world/x-vrml");
		openTypeMap.put(".wrz", "x-world/x-vrml");
		openTypeMap.put(".ws", "text/vnd.wap.wmlscript");
		openTypeMap.put(".wsc", "application/vnd.wap.wmlscriptc");
		openTypeMap.put(".wv", "video/wavelet");
		openTypeMap.put(".wvx", "video/x-ms-wvx");
		openTypeMap.put(".wxl", "application/x-wxl");
		openTypeMap.put(".x-gzip", "application/x-gzip");
		openTypeMap.put(".xar", "application/vnd.xara");
		openTypeMap.put(".xbm", "image/x-xbitmap");
		openTypeMap.put(".xdm", "application/x-xdma");
		openTypeMap.put(".xdma", "application/x-xdma");
		openTypeMap.put(".xdw", "application/vnd.fujixerox.docuworks");
		openTypeMap.put(".xht", "application/xhtml+xml");
		openTypeMap.put(".xhtm", "application/xhtml+xml");
		openTypeMap.put(".xhtml", "application/xhtml+xml");
		openTypeMap.put(".xla", "application/vnd.ms-excel");
		openTypeMap.put(".xlc", "application/vnd.ms-excel");
		openTypeMap.put(".xll", "application/x-excel");
		openTypeMap.put(".xlm", "application/vnd.ms-excel");
		openTypeMap.put(".xls", "application/vnd.ms-excel");
		openTypeMap.put(".xlsx", "application/vnd.ms-excel");
		openTypeMap.put(".xlt", "application/vnd.ms-excel");
		openTypeMap.put(".xlw", "application/vnd.ms-excel");
		openTypeMap.put(".xm", "audio/x-mod");
		openTypeMap.put(".xml", "text/xml");
		openTypeMap.put(".xmz", "audio/x-mod");
		openTypeMap.put(".xpi", "application/x-xpinstall");
		openTypeMap.put(".xpm", "image/x-xpixmap");
		openTypeMap.put(".xsit", "text/xml");
		openTypeMap.put(".xsl", "text/xml");
		openTypeMap.put(".xul", "text/xul");
		openTypeMap.put(".xwd", "image/x-xwindowdump");
		openTypeMap.put(".xyz", "chemical/x-pdb");
		openTypeMap.put(".yz1", "application/x-yz1");
		openTypeMap.put(".z", "application/x-compress");
		openTypeMap.put(".zac", "application/x-zaurus-zac");
		openTypeMap.put(".zip", "application/zip");
		openTypeMap.put("", "*/*");
	}

}
