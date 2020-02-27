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
	 * Canny処理のしきい値1.
	 */
	public JSlider cannyThreshold1Slider;

	/**
	 * Canny処理のしきい値2.
	 */
	public JSlider cannyThreshold2Slider;

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
		JPanel lineAxisPanel1 = new JPanel();
		lineAxisPanel1.setLayout(new BoxLayout(lineAxisPanel1, BoxLayout.LINE_AXIS));
		pageAxisPanel.add(lineAxisPanel1);

		// コンポーネントを追加
		lineAxisPanel1.add(new JLabel("gaussianBlueSize"));
		lineAxisPanel1.add(gaussianBlueSizeSlider);

		// 左から右へレイアウトするパネルを作成
		JPanel lineAxisPanel2 = new JPanel();
		lineAxisPanel2.setLayout(new BoxLayout(lineAxisPanel2, BoxLayout.LINE_AXIS));
		pageAxisPanel.add(lineAxisPanel2);

		// コンポーネントを追加
		lineAxisPanel2.add(new JLabel("cannyThreshold1"));
		lineAxisPanel2.add(cannyThreshold1Slider);

		// 左から右へレイアウトするパネルを作成
		JPanel lineAxisPanel3 = new JPanel();
		lineAxisPanel3.setLayout(new BoxLayout(lineAxisPanel3, BoxLayout.LINE_AXIS));
		pageAxisPanel.add(lineAxisPanel3);

		// コンポーネントを追加
		lineAxisPanel3.add(new JLabel("cannyThreshold2"));
		lineAxisPanel3.add(cannyThreshold2Slider);

		// 左から右へレイアウトするパネルを作成
		JPanel lineAxisPanel4 = new JPanel();
		lineAxisPanel4.setLayout(new BoxLayout(lineAxisPanel4, BoxLayout.LINE_AXIS));
		pageAxisPanel.add(lineAxisPanel4);

		// コンポーネントを追加
		lineAxisPanel4.add(new JLabel("contourAreaMinThreshold"));
		lineAxisPanel4.add(contourAreaMinThresholdSlider);

		// 左から右へレイアウトするパネルを作成
		JPanel lineAxisPanel5 = new JPanel();
		lineAxisPanel5.setLayout(new BoxLayout(lineAxisPanel5, BoxLayout.LINE_AXIS));
		pageAxisPanel.add(lineAxisPanel5);

		// コンポーネントを追加
		lineAxisPanel5.add(new JLabel("contourAreaMaxThreshold"));
		lineAxisPanel5.add(contourAreaMaxThresholdSlider);

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
		parameter.cannyThreshold1 = cannyThreshold1Slider.getValue();
		parameter.cannyThreshold2 = cannyThreshold2Slider.getValue();
		parameter.contourAreaMinThreshold = contourAreaMinThresholdSlider.getValue();
		parameter.contourAreaMaxThreshold = contourAreaMaxThresholdSlider.getValue();
		return parameter;
	}
}