package com.gadomski.dennis;

import com.gadomski.dennis.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Oscillator extends SynthControlContainer {
    private static final int TONE_OFFSET_LIMIT = 2000;

    public Random random = new Random();

    private int wavePos;
    private double keyFrequency;
    private double frequency;
    private int toneOffset;
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

        JLabel toneParameter = new JLabel("x0.00");
        toneParameter.setBounds(165, 65, 50, 25);
        toneParameter.setBorder(Utils.WindowDesign.LINE_BORDER);
        toneParameter.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                final Cursor BLANK_CURSOR = Toolkit
                        .getDefaultToolkit()
                        .createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
                                new Point(0,0), "blank_cursor");
                setCursor(BLANK_CURSOR);
                mouseClickLocation = e.getLocationOnScreen();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });

        toneParameter.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(mouseClickLocation.y != e.getYOnScreen()) {
                    boolean mouseMovingUp = mouseClickLocation.y - e.getYOnScreen() > 0;

                    if(mouseMovingUp && toneOffset < TONE_OFFSET_LIMIT) {
                        ++toneOffset;
                    } else if (!mouseMovingUp && toneOffset > -TONE_OFFSET_LIMIT){
                        --toneOffset;
                    }
                    applyToneOffset();
                    toneParameter.setText("x" + getToneOffset());
                }
                Utils.ParamaterHandling.PARAMETER_ROBOT.mouseMove(mouseClickLocation.x,mouseClickLocation.y);
            }
        });

        add(toneParameter);
        JLabel toneText = new JLabel("Tone");
        toneText.setBounds(172, 40, 75, 25);
        add(toneText);
        setSize(270, 100);
        setBorder(Utils.WindowDesign.LINE_BORDER);
        setLayout(null);
    }

    private enum WaveForm {
        SINE, SQUARE, SAV, TRIANGLE, NOISE
    }

    public double getKeyFrequency() {
        return frequency;
    }

    public void setKeyFrequencies(double frequency){
        keyFrequency = this.frequency = frequency;
        applyToneOffset();
    }

    private double getToneOffset() {
        return toneOffset / 1000d;
    }

    public double nextSample() {
        double tDivP = (wavePos++ / (double) SynthBase.AudioInfo.SAMPLE_RATE) / (1d / frequency);
        switch(waveForm) {
            case SINE:
                return Math.sin(Utils.Math.frequencyToAngularFrequency(frequency) * (wavePos - 1) / SynthBase.AudioInfo.SAMPLE_RATE);
            case SQUARE:
                return Math.signum(Math.sin(Utils.Math.frequencyToAngularFrequency(frequency) * (wavePos - 1) / SynthBase.AudioInfo.SAMPLE_RATE));
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

    private void applyToneOffset() {
        frequency = keyFrequency * Math.pow(2, getToneOffset());
        System.out.println(frequency);
    }
}
