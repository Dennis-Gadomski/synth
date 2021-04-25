package com.gadomski.dennis;

import javax.swing.*;
import java.awt.*;

public class SynthControlContainer extends JPanel {
    protected boolean on;
    private SynthBase synth;

    public SynthControlContainer(SynthBase synthBase) {
        synth = synthBase;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    @Override
    public Component add(Component comp) {
        comp.addKeyListener(synth.getKeyAdapter());
        return super.add(comp);
    }

    @Override
    public Component add(Component comp, int index) {
        comp.addKeyListener(synth.getKeyAdapter());
        return super.add(comp, index);
    }

    @Override
    public Component add(String name, Component comp) {
        comp.addKeyListener(synth.getKeyAdapter());
        return super.add(name, comp);
    }

    @Override
    public void add(Component comp, Object constraints) {
        comp.addKeyListener(synth.getKeyAdapter());
        super.add(comp, constraints);
    }

    @Override
    public void add(Component comp, Object constraints, int index) {
        comp.addKeyListener(synth.getKeyAdapter());
        super.add(comp, constraints, index);
    }
}
