package com.company;

import java.util.*;
import java.io.*;


/**
 * Created by madhu on 6/14/16.
 */
public class GradDescent {
    public static final double uu = 99;
    private double alpha;
    private double beta;

    private double getAlpha() {
        return alpha;
    }

    private double getBeta() {
        return beta;
    }

    private double calculateGradDescent(double alpha, double beta)
    {
        this.alpha = alpha;
        this.beta = beta;
        return alpha;

    }

    private double deduceHypoAnswer(double i) {
        return alpha + beta * i;

    }

    private double calculateResult(double[][] predictorData, boolean FactorEnable) {
        double result = 0;
        for (int i = 0; i < predictorData.length; i++) {
            result = (deduceHypoAnswer(predictorData[i][0] - predictorData[i][1]));
            if (FactorEnable) result = result * predictorData[i][0];

        }
        return result;
    }


    public void trainData(double learningRate, double[][] predictorData) {
        int loop = 0;
        double gamma, delta;
        do {
            loop++;
            System.out.println("PRECISELY: " + (learningRate * ((double) 1 / predictorData.length)) * calculateResult(predictorData, false));
            double neg1 = alpha - learningRate * (((double) 1 / predictorData.length) * calculateResult(predictorData, false));
            double neg2 = beta - learningRate * (((double) 1 / predictorData.length) * calculateResult(predictorData, true));
            gamma = alpha - neg1;
            delta = beta - neg2;
            alpha = neg1;
            beta = neg2;
        }
        while ((Math.abs(gamma) + Math.abs(delta)) > uu);
        System.out.println(loop);

    }

    private static final double[][] GIVENDATA = {{200, 20000}, {300, 41000}, {900, 141000}, {800, 41000}, {400, 51000}, {500, 61500}};

    public static void main(String[] args) {
        GradDescent gd = new GradDescent();
        gd.trainData(0.00001, GIVENDATA);
        System.out.println("ALPHA: " + gd.getAlpha() + " - BETA: " + gd.getBeta());
        System.out.println("PREDICTION: " + gd.deduceHypoAnswer(300));
    }
}








