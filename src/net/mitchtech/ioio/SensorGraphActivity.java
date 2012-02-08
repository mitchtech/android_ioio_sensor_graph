package net.mitchtech.ioio;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;
import net.mitchtech.ioio.sensorgraph.R;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class SensorGraphActivity extends AbstractIOIOActivity {
	private final int ANALOG_SENSOR_PIN = 34;

	private GraphView mGraphView;
	TextView mTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		mGraphView = (GraphView) findViewById(R.id.graph);
		mGraphView.setMaxValue(100);
		mTextView = (TextView) findViewById(R.id.value);
	}

	class IOIOThread extends AbstractIOIOActivity.IOIOThread {
		private AnalogInput mInput;

		@Override
		public void setup() throws ConnectionLostException {
			try {
				mInput = ioio_.openAnalogInput(ANALOG_SENSOR_PIN);
			} catch (ConnectionLostException e) {
				throw e;
			}
		}

		@Override
		public void loop() throws ConnectionLostException {
			try {
				final float reading = mInput.read();
				addPoint(reading * 100);
				setText(Float.toString((reading * 100)));
				sleep(10);
			} catch (InterruptedException e) {
				ioio_.disconnect();
			} catch (ConnectionLostException e) {
				throw e;
			}
		}
	}

	@Override
	protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
		return new IOIOThread();
	}

	private void setText(final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mTextView.setText(str);
			}
		});
	}

	private void addPoint(final float point) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mGraphView.addDataPoint(point);
			}
		});
	}
}