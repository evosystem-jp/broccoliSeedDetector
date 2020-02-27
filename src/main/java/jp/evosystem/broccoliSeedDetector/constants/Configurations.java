package jp.evosystem.broccoliSeedDetector.constants;

/**
 * 環境設定.
 *
 * @author evosystem
 */
public interface Configurations {

	/**
	 * デバッグモードを有効化するかどうか.
	 */
	boolean ENABLE_DEBUG_MODE = false;

	/**
	 * ビープ音を鳴らすまでの連続NGフレーム数.
	 */
	int BEEP_THRESHOLD_NG_COUNT = 30;

	/**
	 * ブラーのサイズのデフォルト値(奇数).
	 */
	int DEFAULT_GAUSSIAN_BLUR_SIZE = 25;

	/**
	 * CLAHEのclipLimitのデフォルト値.
	 */
	double DEFAULT_CLAHE_CLIP_LIMIT = 40;

	/**
	 * CLAHEのタイルサイズのデフォルト値.
	 */
	int DEFAULT_CLAHE_TILE_GRID_SIZE = 8;

	/**
	 * エッジ抽出処理のしきい値1のデフォルト値.
	 */
	int DEFAULT_CANNY_THRESHOLD_1 = 25;

	/**
	 * エッジ抽出処理のしきい値2のデフォルト値.
	 */
	int DEFAULT_CANNY_THRESHOLD_2 = 50;

	/**
	 * 膨張処理の繰り返し回数のデフォルト値.
	 */
	int DEFAULT_DILATE_ITERATIONS = 5;

	/**
	 * 収縮処理の繰り返し回数のデフォルト値.
	 */
	int DEFAULT_ERODE_ITERATIONS = 4;

	/**
	 * 輪郭の面積の最小値のデフォルト値.
	 */
	int DEFAULT_CONTOUR_AREA_MIN_THRESHOLD = 20000;

	/**
	 * 輪郭の面積の最大値のデフォルト値.
	 */
	int DEFAULT_CONTOUR_AREA_MAX_THRESHOLD = 30000;

	/**
	 * 全ての輪郭を描画するかどうか.
	 */
	boolean DRAW_ALL_CONTOURS = false;

	/**
	 * 処理結果をファイルに出力するかどうか.
	 */
	boolean ENABLE_RECORDING = true;

	/**
	 * 対象の画像ファイルのパス.
	 */
	String TARGET_IMAGE_FILE_PATH = "images/example.jpg";

	/**
	 * 対象の動画ファイルのパス.
	 */
	String TARGET_VIDEO_FILE_PATH = "videos/example.mov";

	/**
	 * 使用するWebカメラのデバイス番号.
	 */
	int TARGET_DEVICE_NUMBER = 0;

	/**
	 * 使用するIPカメラのURL.
	 */
	String TARGET_IP_CAMERA_URL = "http://61.214.197.204:1024/-wvhttp-01-/GetOneShot?image_size=640x480&frame_count=1000000000";
}