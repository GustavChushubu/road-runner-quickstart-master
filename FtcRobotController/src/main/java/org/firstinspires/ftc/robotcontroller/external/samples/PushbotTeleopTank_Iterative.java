package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="12313123", group="Pushbot")
public class PushbotTeleopTank_Iterative extends OpMode {
    DcMotor leftFront;
    DcMotor leftRear;
    DcMotor rightRear;
    DcMotor rightFront;
    DcMotor intake;
    DcMotor lift;
    DcMotor lift2;
    DcMotor spinner;
    Servo dumper;
    boolean wasX = false;
    boolean isX = false;
    boolean wasA = false;
    boolean isA = false;
    float sloMo = 1;

    public void init() {
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        lift = hardwareMap.get(DcMotorEx.class, "lift");
        lift2 = hardwareMap.get(DcMotorEx.class, "lift2");
        spinner = hardwareMap.get(DcMotorEx.class, "spinner");
        dumper = hardwareMap.get(Servo.class, "dumper");

        //lift2.setDirection(DcMotor.Direction.REVERSE);
        //rightRear.setDirection(DcMotor.Direction.REVERSE);
        //rightFront.setDirection(DcMotor.Direction.REVERSE);
    }

    public void loop() {
        isX = gamepad1.x;
        isA = gamepad1.a;


        double rx = -gamepad1.left_stick_y; // Remember, this is reversed!
        double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double y = gamepad1.right_stick_x;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = ((y + x + rx)*sloMo) / denominator;
        double backLeftPower = ((y - x + rx)*sloMo) / denominator;
        double frontRightPower = ((y - x - rx)*sloMo) / denominator;
        double backRightPower = ((y + x - rx)*sloMo) / denominator;

        leftFront.setPower(frontLeftPower);
        leftRear.setPower(backLeftPower);
        rightRear.setPower(backRightPower);
        rightFront.setPower(frontRightPower);
        if(gamepad1.x)
        {
            spinner.setPower(-1);
        }
        else if(gamepad1.y)
        {
             spinner.setPower(0);
        }
        else if(gamepad1.b)
        {
            spinner.setPower(1);
        }
        //slomo
        if(isX == true && wasX == false)
        {
            if(sloMo == 1f)
            {
                sloMo = .5f;
            }
            else if(sloMo == .5f)
            {
                sloMo = .2f;
            }
            else if(sloMo == .2f)
            {
                sloMo = 1f;
            }
        }

        lift.setPower(gamepad2.right_trigger - gamepad2.left_trigger);
        lift2.setPower(gamepad2.right_trigger - gamepad2.left_trigger);
        intake.setPower(gamepad2.left_stick_y);
        if(gamepad2.dpad_left){
            dumper.setPosition(.6);
        }
        else if(gamepad2.dpad_up){
            dumper.setPosition(0);
        }
        else if(gamepad2.dpad_down){
            dumper.setPosition(1);
        }
        else if(gamepad2.dpad_right){
            dumper.setPosition(.4);
        }
        telemetry.addData("SloMo", sloMo);
        wasX = isX;
        wasA = isA;
    }
}