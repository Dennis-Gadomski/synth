package com.gadomski.dennis;

import com.gadomski.dennis.utils.Utils;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.Random;

public class Oscillator extends SynthControlContainer {
    public Random random = new Random();
    private static final double FREQUENCY = 500;
    private int wavePos;

    private WaveForm waveForm = WaveForm.SINE;

    public Oscillator (SynthBase synth) {
        super(synth);
        JComboBox<WaveForm> comboBox = new JComboBox<>(new WaveForm[]{WaveForm.SINE, WaveForm.SQUARE, WaveForm.SAV, WaveForm.TRIANGLE, WaveForm.NOISE});
        comboBox.setSelectedItem(WaveForm.SINE);
        comboBox.setBounds(10, 10, 120, 25);
        comboBox.addItemListener(i -> {
            if(i.getStateChange() == ItemEvent.SELECTED) {
                waveForm = (WaveForm) i.getItem();
            }
        });
        add(comboBox);
        setSize(270, 100);
        setBorder(Utils.WindowDesign.LINE_BORDER);
        setLayout(null);
    }

    private enum WaveForm {
        SINE, SQUARE, SAV, TRIANGLE, NOISE
    }

    public double nextSample() {
        double tDivP = (wavePos++ / (double) SynthBase.AudioInfo.SAMPLE_RATE) / (1d /FREQUENCY);
        switch(waveForm) {
            case SINE:
                return Math.sin(Utils.Math.frequencyToAngularFrequency(FREQUENCY) * (wavePos - 1) / SynthBase.AudioInfo.SAMPLE_RATE);
            case SQUARE:
                return Math.signum(Math.sin(Utils.Math.frequencyToAngularFrequency(FREQUENCY) * (wavePos - 1) / SynthBase.AudioInfo.SAMPLE_RATE));
            case SAV:
                return 2d * (tDivP - Math.floor(0.5 + tDivP));
            case TRIANGLE:
                return 2d * Math.abs(2d * (tDivP - Math.floor(0.5 + tDivP))) - 1;
            case NOISE:
                return random.nextDouble();
            default:
                throw new RuntimeException("Oscillator broken");
        }
    }
}
