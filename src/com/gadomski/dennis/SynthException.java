package com.gadomski.dennis;

public class SynthException extends RuntimeException{
    SynthException(int errCode) {
         super("Synth error: " + errCode );
    }
}
