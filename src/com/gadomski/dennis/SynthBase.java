package com.gadomski.dennis;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SynthBase {
    private boolean shouldGenerate;

    private final Oscillator[] oscillators = new Oscillator[3];
    private final JFrame frame = new JFrame("Gadomski's Synth");
    private final AudioThread audioThread = new AudioThread(() -> {

        if(!shouldGenerate) {
            return null;
        }
        short[] s = new short[AudioThread.BUFFER_SIZE];
        for(int i = 0; i<AudioThread.BUFFER_SIZE; ++i) {
            double d = 0;
            for(Oscillator o: oscillators) {
                d += o.nextSample() / oscillators.length;
            }
            s[i] = (short) (Short.MAX_VALUE * d);
        }
        return s;
    });

    public KeyAdapter getKeyAdapter() {
        return this.keyAdapter;
    }

    private final KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if(!audioThread.isRunning()) {
                System.out.println("Clicked");
                shouldGenerate = true;
                audioThread.triggeredPlayback();

            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            shouldGenerate = false;
        }
    };

    public SynthBase() {
        int y = 0;
        for(int i = 0; i< oscillators.length; ++i ){
            oscillators[i] = new Oscillator(this);
            oscillators[i].setLocation(5, y);
            frame.add(oscillators[i]);
            y += 105;
        }

        frame.addKeyListener(keyAdapter);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                audioThread.close();
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(600, 350);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static class AudioInfo {
        public static final int SAMPLE_RATE = 44100;
    }
}