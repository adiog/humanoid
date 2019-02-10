// This file is a part of Humanoid project.
// Copyright (C) 2018 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid.sensor;


class KalmanInternal {
    private float Q_angle;    // Process noise variance for the accelerometer
    private float Q_bias;     // Process noise variance for the gyro bias
    private float R_measure;  // Measurement noise variance - this is actually the variance of the measurement noise

    private float angle;  // The angle calculated by the Kalman filter - part of the 2x1 state vector
    private float bias;   // The gyro bias calculated by the Kalman filter - part of the 2x1 state vector
    private float rate;   // Unbiased rate calculated from the rate and the calculated bias - you have to call getAngle to update the rate

    private float[][] errorCovarianceMatrix = new float[2][2];

    KalmanInternal() {
        /* We will set the variables like so, these can also be tuned by the user */
        Q_angle = 0.001f;
        Q_bias = 0.003f;
        R_measure = 0.03f;

        angle = 0.0f; // Reset the angle
        bias = 0.0f; // Reset bias

        errorCovarianceMatrix[0][0] = 0.0f;
        errorCovarianceMatrix[0][1] = 0.0f;
        errorCovarianceMatrix[1][0] = 0.0f;
        errorCovarianceMatrix[1][1] = 0.0f;
    }

    float getAngle(float newAngle, float newRate, float dt) {
        rate = newRate - bias;
        angle += dt * rate;

        // Update estimation error covariance - Project the error covariance ahead
        /* Step 2 */
        errorCovarianceMatrix[0][0] += dt * (dt * errorCovarianceMatrix[1][1] - errorCovarianceMatrix[0][1] - errorCovarianceMatrix[1][0] + Q_angle);
        errorCovarianceMatrix[0][1] -= dt * errorCovarianceMatrix[1][1];
        errorCovarianceMatrix[1][0] -= dt * errorCovarianceMatrix[1][1];
        errorCovarianceMatrix[1][1] += Q_bias * dt;

        // Discrete Kalman filter measurement update equations - Measurement Update ("Correct")
        // Calculate Kalman gain - Compute the Kalman gain
        /* Step 4 */
        float S = errorCovarianceMatrix[0][0] + R_measure; // Estimate error
        /* Step 5 */
        float[] K = new float[2]; // Kalman gain - This is a 2x1 vector
        K[0] = errorCovarianceMatrix[0][0] / S;
        K[1] = errorCovarianceMatrix[1][0] / S;

        // Calculate angle and bias - Update estimate with measurement zk (newAngle)
        /* Step 3 */
        float y = newAngle - angle; // Angle difference
        /* Step 6 */
        angle += K[0] * y;
        bias += K[1] * y;

        // Calculate estimation error covariance - Update the error covariance
        /* Step 7 */
        float P00_temp = errorCovarianceMatrix[0][0];
        float P01_temp = errorCovarianceMatrix[0][1];

        errorCovarianceMatrix[0][0] -= K[0] * P00_temp;
        errorCovarianceMatrix[0][1] -= K[0] * P01_temp;
        errorCovarianceMatrix[1][0] -= K[1] * P00_temp;
        errorCovarianceMatrix[1][1] -= K[1] * P01_temp;

        return angle;
    }

    void setAngle(float angle) {
        this.angle = angle;
    }
}
