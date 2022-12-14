package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class BlazinNov3 extends OpMode
{
    private double powerLX, powerLY, powerRX, powerRY;
    private DcMotor FL, BL, FR, BR;
    public Servo claw;
    public DcMotor linear;
    double lcpi = 128;
    int inch = 0;
    //14, 24, 34

    @Override
    public void init() {
        FR = hardwareMap.get(DcMotor.class, "FR");
        FL = hardwareMap.get(DcMotor.class, "FL");
        BR = hardwareMap.get(DcMotor.class, "BR");
        BL = hardwareMap.get(DcMotor.class, "BL");

        BR.setDirection(DcMotorSimple.Direction.FORWARD);
        FR.setDirection(DcMotorSimple.Direction.FORWARD);
        BL.setDirection(DcMotorSimple.Direction.REVERSE);
        FL.setDirection(DcMotorSimple.Direction.REVERSE);

        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);

        linear = hardwareMap.get(DcMotor.class, "L");
        claw = hardwareMap.get(Servo.class, "c");
        linear.setDirection(DcMotorSimple.Direction.REVERSE);
        linear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        linear.setPower(0);
        claw.setPosition(0);

    }

    @Override
    public void loop() {
        powerLX = gamepad1.left_stick_x/2;
        powerLY = gamepad1.left_stick_y/2;
        powerRX = gamepad1.right_stick_x/2;
        powerRY = gamepad1.right_stick_y/2;

        if (gamepad1.right_bumper || gamepad1.left_bumper)
        {
            powerLX = gamepad1.left_stick_x/3;
            powerLY = gamepad1.left_stick_y/3;
            powerRX = gamepad1.right_stick_x/3;
            powerRY = gamepad1.right_stick_y/3;
        }

        if (powerLY > 0.07 || powerLY < -0.07) {
            FL.setPower(powerLY);
            BL.setPower(powerLY);
        } else {
            FL.setPower(0);
            BL.setPower(0);
        }

        if (powerRY > 0.07 || powerRY < -0.07) {
            FR.setPower(powerRY);
            BR.setPower(powerRY);
        } else {
            FR.setPower(0);
            BR.setPower(0);
        }

        if (gamepad2.dpad_down && inch != 0)
        {
            lineardown(inch, 0.6);
            inch = 0;
        }
        else if (gamepad2.dpad_left && inch == 0)
        {
            linearup(16, 0.7);
            inch = 16;
        }
        else if (gamepad2.dpad_left && inch > 16)
        {
            lineardown(inch - 16, 0.4);
            inch = 16;
        }
        else if (gamepad2.dpad_up && inch > 26)
        {
            lineardown(inch - 26, 0.4);
            inch = 26;
        }
        else if (gamepad2.dpad_up && inch < 26)
        {
            linearup(26-inch, 0.7);
            inch = 26;
        }
        else if (gamepad2.dpad_right)
        {
            linearup(35-inch, 0.7);
            inch = 35;
        }

            if (gamepad2.a) {
                claw.setPosition(0.45);
            } else if (gamepad2.y) {
                claw.setPosition(0);
            }
    }

    private void linearup(double inch, double power)
    {
        int a = (int) (linear.getCurrentPosition() + (inch*lcpi));
        linear.setTargetPosition(a);
        linear.setPower(power);
        linear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (linear.isBusy())
        {

        }
        if(inch < 8)
        {
            linear.setPower(0.1);
        }else if (inch >= 8 && inch < 18)
        {
            linear.setPower(0.2);
        }else if(inch >= 18)
        {
            linear.setPower(0.3);
        }
    }
    private void lineardown(double inch, double power)
    {
        int a = (int) (linear.getCurrentPosition() - (inch*lcpi));
        linear.setTargetPosition(a);
        linear.setPower(power);
        linear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (linear.isBusy())
        {

        }
        linear.setPower(0);
    }
}
