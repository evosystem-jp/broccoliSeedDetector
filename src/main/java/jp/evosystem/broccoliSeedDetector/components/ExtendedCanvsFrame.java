package jp.evosystem.broccoliSeedDetector.components;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.DisplayMode;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.bytedeco.javacv.CanvasFrame;

import jp.evosystem.broccoliSeedDetector.constants.Configurations;
import jp.evosystem.broccoliSeedDetector.models.ProcessImageParameter;

/**
 * 拡張CanvasFrame.
 *
 * @author evosystem
 */
public class ExtendedCanvsFrame extends CanvasFrame {

	/**
	 * ブラーのサイズ.
	 */
	private JSlider gaussianBlueSizeSlider;

	/**
	 * CLAHEのclipLimit.
	 */
	public JSlider claheClipLimitSlider;

	/**
	 * CLAHEのタイルサイズ.
	 */
	public JSlider claheTileGridSizeSlider;

	/**
	 * エッジ抽出処理のしきい値1.
	 */
	public JSlider cannyThreshold1Slider;

	/**
	 * エッジ抽出処理のしきい値2.
	 */
	public JSlider cannyThreshold2Slider;

	/**
	 * 膨張処理の繰り返し回数.
	 */
	public JSlider dilateIterationsSlider;

	/**
	 * 収縮処理の繰り返し回数.
	 */
	public JSlider erodeIterationsSlider;

	/**
	 * 輪郭の面積の最小値.
	 */
	public JSlider contourAreaMinThresholdSlider;

	/**
	 * 輪郭の面積の最小値.
	 */
	public JSlider contourAreaMaxThresholdSlider;

	/**
	 * コンストラクタ.
	 *
	 * @param title
	 * @param gamma
	 */
	public ExtendedCanvsFrame(String title, double gamma) {
		super(title, gamma);
	}

	@Override
	protected void initCanvas(boolean fullScreen, DisplayMode displayMode, double gamma) {
		super.initCanvas(fullScreen, displayMode, gamma);

		// スライダーを作成
		gaussianBlueSizeSlider = new JSlider(0, 100, Configurations.DEFAULT_GAUSSIAN_BLUR_SIZE);
		gaussianBlueSizeSlider
				.setLabelTable(gaussianBlueSizeSlider.createStandardLabels(gaussianBlueSizeSlider.getMaximum() / 10));
		gaussianBlueSizeSlider.setPaintLabels(true);

		// スライダーを作成
		claheClipLimitSlider = new JSlider(0, 100, (int) Configurations.DEFAULT_CLAHE_CLIP_LIMIT);
		claheClipLimitSlider
				.setLabelTable(claheClipLimitSlider.createStandardLabels(claheClipLimitSlider.getMaximum() / 10));
		claheClipLimitSlider.setPaintLabels(true);

		// スライダーを作成
		claheTileGridSizeSlider = new JSlider(1, 10, (int) Configurations.DEFAULT_CLAHE_TILE_GRID_SIZE);
		claheTileGridSizeSlider
				.setLabelTable(claheTileGridSizeSlider.createStandardLabels(claheTileGridSizeSlider.getMaximum() / 10));
		claheTileGridSizeSlider.setPaintLabels(true);

		// スライダーを作成
		cannyThreshold1Slider = new JSlider(0, 255, Configurations.DEFAULT_CANNY_THRESHOLD_1);
		cannyThreshold1Slider
				.setLabelTable(cannyThreshold1Slider.createStandardLabels(cannyThreshold1Slider.getMaximum() / 10));
		cannyThreshold1Slider.setPaintLabels(true);

		// スライダーを作成
		cannyThreshold2Slider = new JSlider(0, 255, Configurations.DEFAULT_CANNY_THRESHOLD_2);
		cannyThreshold2Slider
				.setLabelTable(cannyThreshold2Slider.createStandardLabels(cannyThreshold2Slider.getMaximum() / 10));
		cannyThreshold2Slider.setPaintLabels(true);

		// スライダーを作成
		dilateIterationsSlider = new JSlider(0, 10, Configurations.DEFAULT_DILATE_ITERATIONS);
		dilateIterationsSlider
				.setLabelTable(dilateIterationsSlider.createStandardLabels(dilateIterationsSlider.getMaximum() / 10));
		dilateIterationsSlider.setPaintLabels(true);

		// スライダーを作成
		erodeIterationsSlider = new JSlider(0, 10, Configurations.DEFAULT_ERODE_ITERATIONS);
		erodeIterationsSlider
				.setLabelTable(erodeIterationsSlider.createStandardLabels(erodeIterationsSlider.getMaximum() / 10));
		erodeIterationsSlider.setPaintLabels(true);

		// スライダーを作成
		contourAreaMinThresholdSlider = new JSlider(0, 50000, Configurations.DEFAULT_CONTOUR_AREA_MIN_THRESHOLD);
		contourAreaMinThresholdSlider.setLabelTable(
				contourAreaMinThresholdSlider.createStandardLabels(contourAreaMinThresholdSlider.getMaximum() / 10));
		contourAreaMinThresholdSlider.setPaintLabels(true);

		// スライダーを作成
		contourAreaMaxThresholdSlider = new JSlider(0, 50000, Configurations.DEFAULT_CONTOUR_AREA_MAX_THRESHOLD);
		contourAreaMaxThresholdSlider.setLabelTable(
				contourAreaMaxThresholdSlider.createStandardLabels(contourAreaMaxThresholdSlider.getMaximum() / 10));
		contourAreaMaxThresholdSlider.setPaintLabels(true);

		// コンポーネントを配置
		Container contentPane = this.getContentPane();

		// 上から下へレイアウトするパネルを作成
		JPanel pageAxisPanel = new JPanel();
		pageAxisPanel.setLayout(new BoxLayout(pageAxisPanel, BoxLayout.PAGE_AXIS));

		// 左から右へレイアウトするパネルを作成
		JPanel gaussianBlueSizeLineAxisPanel = new JPanel();
		gaussianBlueSizeLineAxisPanel.setLayout(new BoxLayout(gaussianBlueSizeLineAxisPanel, BoxLayout.LINE_AXIS));
		pageAxisPanel.add(gaussianBlueSizeLineAxisPanel);

		// コンポーネントを追加
		gaussianBlueSizeLineAxisPanel.add(new JLabel("gaussianBlueSize"));
		gaussianBlueSizeLineAxisPanel.add(gaussianBlueSizeSlider);

		// 左から右へレイアウトするパネルを作成
		JPanel claheClipLimitLineAxisPanel = new JPanel();
		claheClipLimitLineAxisPanel.setLayout(new BoxLayout(claheClipLimitLineAxisPanel, BoxLayout.LINE_AXIS));
		pageAxisPanel.add(claheClipLimitLineAxisPanel);

		// コンポーネントを追加
		claheClipLimitLineAxisPanel.add(new JLabel("claheClipLimit"));
		claheClipLimitLineAxisPanel.add(claheClipLimitSlider);

		// 左から右へレイアウトするパネルを作成
		JPanel claheTileGridSizeLineAxisPanel = new JPanel();
		claheTileGridSizeLineAxisPanel.setLayout(new BoxLayout(claheTileGridSizeLineAxisPanel, BoxLayout.LINE_AXIS));
		pageAxisPanel.add(claheTileGridSizeLineAxisPanel);

		// コンポーネントを追加
		claheTileGridSizeLineAxisPanel.add(new JLabel("claheTileGridSize"));
		claheTileGridSizeLineAxisPanel.add(claheTileGridSizeSlider);

		// 左から右へレイアウトするパネルを作成
		JPanel cannyThreshold1LineAxisPanel = new JPanel();
		cannyThreshold1LineAxisPanel.setLayout(new BoxLayout(cannyThreshold1LineAxisPanel, BoxLayout.LINE_AXIS));
		pageAxisPanel.add(cannyThreshold1LineAxisPanel);

		// コンポーネントを追加
		cannyThreshold1LineAxisPanel.add(new JLabel("cannyThreshold1"));
		cannyThreshold1LineAxisPanel.add(cannyThreshold1Slider);

		// 左から右へレイアウトするパネルを作成
		JPanel cannyThreshold2LineAxisPanel = new JPanel();
		cannyThreshold2LineAxisPanel.setLayout(new BoxLayout(cannyThreshold2LineAxisPanel, BoxLayout.LINE_AXIS));
		pageAxisPanel.add(cannyThreshold2LineAxisPanel);

		// コンポーネントを追加
		cannyThreshold2LineAxisPanel.add(new JLabel("cannyThreshold2"));
		cannyThreshold2LineAxisPanel.add(cannyThreshold2Slider);

		// 左から右へレイアウトするパネルを作成
		JPanel dilateIterationsLineAxisPanel = new JPanel();
		dilateIterationsLineAxisPanel.setLayout(new BoxLayout(dilateIterationsLineAxisPanel, BoxLayout.LINE_AXIS));
		pageAxisPanel.add(dilateIterationsLineAxisPanel);

		// コンポーネントを追加
		dilateIterationsLineAxisPanel.add(new JLabel("dilateIterations"));
		dilateIterationsLineAxisPanel.add(dilateIterationsSlider);

		// 左から右へレイアウトするパネルを作成
		JPanel erodeIterationsLineAxisPanel = new JPanel();
		erodeIterationsLineAxisPanel.setLayout(new BoxLayout(erodeIterationsLineAxisPanel, BoxLayout.LINE_AXIS));
		pageAxisPanel.add(erodeIterationsLineAxisPanel);

		// コンポーネントを追加
		erodeIterationsLineAxisPanel.add(new JLabel("erodeIterations"));
		erodeIterationsLineAxisPanel.add(erodeIterationsSlider);

		// 左から右へレイアウトするパネルを作成
		JPanel contourAreaMinThresholdLineAxisPanel = new JPanel();
		contourAreaMinThresholdLineAxisPanel.setLayout(new BoxLayout(contourAreaMinThresholdLineAxisPanel, BoxLayout.LINE_AXIS));
		pageAxisPanel.add(contourAreaMinThresholdLineAxisPanel);

		// コンポーネントを追加
		contourAreaMinThresholdLineAxisPanel.add(new JLabel("contourAreaMinThreshold"));
		contourAreaMinThresholdLineAxisPanel.add(contourAreaMinThresholdSlider);

		// 左から右へレイアウトするパネルを作成
		JPanel contourAreaMaxThresholdLineAxisPanel = new JPanel();
		contourAreaMaxThresholdLineAxisPanel.setLayout(new BoxLayout(contourAreaMaxThresholdLineAxisPanel, BoxLayout.LINE_AXIS));
		pageAxisPanel.add(contourAreaMaxThresholdLineAxisPanel);

		// コンポーネントを追加
		contourAreaMaxThresholdLineAxisPanel.add(new JLabel("contourAreaMaxThreshold"));
		contourAreaMaxThresholdLineAxisPanel.add(contourAreaMaxThresholdSlider);

		contentPane.add(pageAxisPanel, BorderLayout.NORTH);
	}

	/**
	 * 画像処理用のパラメータを取得.
	 *
	 * @return
	 */
	public ProcessImageParameter getCurrentParameter() {
		ProcessImageParameter parameter = new ProcessImageParameter();
		parameter.gaussianBlueSize = gaussianBlueSizeSlider.getValue();
		if (parameter.gaussianBlueSize % 2 == 0) {
			parameter.gaussianBlueSize++;
		}
		parameter.claheClipLimit = claheClipLimitSlider.getValue();
		parameter.claheTileGridSize = claheTileGridSizeSlider.getValue();
		parameter.cannyThreshold1 = cannyThreshold1Slider.getValue();
		parameter.cannyThreshold2 = cannyThreshold2Slider.getValue();
		parameter.dilateIterations = dilateIterationsSlider.getValue();
		parameter.erodeIterations = erodeIterationsSlider.getValue();
		parameter.contourAreaMinThreshold = contourAreaMinThresholdSlider.getValue();
		parameter.contourAreaMaxThreshold = contourAreaMaxThresholdSlider.getValue();
		return parameter;
	}
}