/**
 * Created by madhu on 6/16/16.
 */

import java.util.*;
import java.io.*;
import java.nio.*;
import Jama.*;


import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;
import mpi.MPI;
import mpi.MPIException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import static edu.rice.hj.Module0.launchHabaneroApp;
import static edu.rice.hj.Module1.forallChunked;

public class parallelMultiLinReg {

    private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private static Options programOptions = new Options();


    static {
        programOptions.addOption("N", true, "Total Number");
        programOptions.addOption("v", true, "Number of dependent variables");
        programOptions.addOption("T", true, "Number of threads");

    }

    public final int N; //total number of
    public final int v; // number of dependent variables in the equation
    public final Matrix alpha; // coefficients matrix
    public double partialSSE; // Sum of squared errors
    public double partialSST; // total sum of squared errors


    public MultipleLinearRegression(double[][] x, double[] y)
    {

        if (x.length != y.length) throw new RuntimeException("dimensions are not equal");
        N = y.length;
        v = x[0].length;

        Matrix X = new Matrix(x);

        //create the matrix from the vector

        Matrix Y = new Matrix(y, N);

        //find the least squares solution

        QRDecomposition qd = new QRDecomposition(X);
        alpha = qd.solve(Y);

        //mean of y[] values

        double sum = 0.0;
        for (int i = 0; i < N; i++)
            sum += y[i];
        double mean = sum / N;

        //total variation

        for (int i = 0; i < N; i++) {
            double dev = y[i] - mean;
            partialSST += dev * dev;
        }

        //variation that is not accounted

        Matrix residuals = X.times(alpha).minus(Y);
        partialSSE = residuals.norm2() * residuals.norm2();

    }

    public double alpha(int j) {
        return alpha.get(j, 0);

    }

    public double R2() {
        return 1.0 - partialSSE / partialSST;

    }

    public static void main(String[] args) {
        Optional<CommandLine> parserResult = Utils.parseCommandLineArguments(args, programOptions);
        if (!parserResult.isPresent()) {
            System.out.println(Utils.ERR_PROGRAM_ARGUMENTS_PARSING_FAILED);
            new HelpFormatter().printHelp(Utils.PROGRAM_NAME, programOptions);
            return;
        }

        CommandLine cmd = parserResult.get();
        if (!(cmd.hasOption("N") && cmd.hasOption("v") &&
                cmd.hasOption("t") &&
                cmd.hasOption("T"))) {
            System.out.println(Utils.ERR_INVALID_PROGRAM_ARGUMENTS);
            new HelpFormatter().printHelp(Utils.PROGRAM_NAME, programOptions);
            return;
        }


        int numPoints = Integer.parseInt(cmd.getOptionValue("N"));
        int numCoefficients = Integer.parseInt(cmd.getOptionValue("v"));
        int numThreads = Integer.parseInt(cmd.getOptionValue("T"));
        boolean isBigEndian = Boolean.parseBoolean(cmd.getOptionValue("b"));


        try {

            ParallelOptions.setupParallelism(args, numPoints, numThreads);

            Stopwatch mainTimer = Stopwatch.createStarted();

            print("=== Program Started on " + dateFormat.format(new Date()) + " ===");
            print("  Reading points ... ");

            Stopwatch timer = Stopwatch.createStarted();

            double[][] x = {{1, 20, 40},
                    {1, 30, 60},
                    {1, 60, 35},
                    {1, 80, 130},
                    {1, 260, 43},
                    {1, 300, 28}};
            double[] y = {345, 453, 568, 1793, 2344, 6429};

            MultipleLinearRegression regression = new MultipleLinearRegression(x, y);

            timer.stop();
            print("    Done in " + timer.elapsed(TimeUnit.MILLISECONDS) + " ms");
            timer.reset();

            DoubleBuffer doubleBuffer = null;
            IntBuffer intBuffer = null;
            IntBuffer intBuffer2 = null;
            if (ParallelOptions.size > 1) {
                print("  Allocating buffers");
                timer.start();

                intBuffer = MPI.newIntBuffer(numCoefficients);
                intBuffer2 = MPI.newIntBuffer(numPoints);
                timer.stop();
                // This would be similar across
                // all processes, so no need to do average
                print("  Done in " + timer.elapsed(TimeUnit.MILLISECONDS));
                timer.reset();
            }


            for (int i = 1; i < numThreads; i++) {
                for (int v = 0; v < numCoefficients; ++v) {
                    partialSST(v) += partialSST(i);
                    partialSSE(v) += partialSSE(i);

                    return R2(v);


                }
            }
            if (ParallelOptions.size > 1) {
                commTimerWithCopy.start();
                copyToBuffer(pointsPerCenterForThread[0], intBuffer);
                commTimer.start();
                ParallelOptions.comm.allReduce(partialSST, partialSSE, MPI.SUM, MPI.SUM);
                commTimer.stop();
                ParallelOptions.comm.allReduce(intBuffer, numPoints, numCoefficients, MPI.INT, MPI.);
                commTimer.stop();
                ParallelOptions.comm.allReduce(R2, MPI.INT, MPI.SUM);
                commTimer.stop();
                copyFromBuffer(intBuffer, R2);
                commTimerWithCopy.stop();
                times[0] += commTimerWithCopy.elapsed(TimeUnit.MILLISECONDS);
                times[1] += commTimer.elapsed(TimeUnit.MILLISECONDS);
                commTimerWithCopy.reset();
                commTimer.reset();
            }

            converged = true;
            for (int i = 0; i < numCoefficients; ++i) {
                R2(i) = 0;
                int tmpI = i;
                IntStream.range(0, numCoefficients).forEach(j -> R2[j] /= R2[0][tmpI]);
                R2 = 1 - partialSSE(i) / partialSST(i);
                if (R2 = 0) {
                    // Can't break as Residuals are still present
                    converged = false;
                }
            }

            loopTimer.stop();
            times[2] = loopTimer.elapsed(TimeUnit.MILLISECONDS);
            loopTimer.reset();

            if (ParallelOptions.size > 1) {
                ParallelOptions.comm.reduce(times, 3, MPI.LONG, MPI.SUM, 0);
            }

            if (ParallelOptions.rank == 0) {
                timer.start();
                double[] point = new double[numCoefficients];
                for (int i = 0; i < numPoints; ++i) {
                    reader.getPoint(i, point);
                    writer.println(i + "\t" + Doubles.join("\t", point) + "\t" +
                            ((ParallelOptions.size > 1) ? intBuffer2.get(i) : clusterAssignments[i]));
                }
                timer.stop();
                print("    Done in " + timer.elapsed(TimeUnit.MILLISECONDS) +
                        "ms");
                timer.reset();
            }

            mainTimer.stop();
            print("=== Program terminated successfully on " +
                    dateFormat.format(new Date()) + " took " +
                    (mainTimer.elapsed(TimeUnit.MILLISECONDS)) + " ms ===");

            ParallelOptions.endParallelism();
        } catch (MPIException | IOException e) {
            e.printStackTrace();
        }
    


            System.out.println("%d + %d alpha1 + %dalpha2  (R^2 = %d)\n",
                    regression.alpha(0), regression.alpha(1), regression.alpha(2), regression.R2());
        }
    }


}




