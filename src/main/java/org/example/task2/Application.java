package org.example.task2;

class RoundHole {
    private int radius;

    public RoundHole(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public boolean fits(RoundStick stick) {
        return this.getRadius() >= stick.getRadius();
    }
}

class RoundStick {
    private int radius;

    public RoundStick(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }
}

class SquareStick {
    private int width;

    public SquareStick(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }
}

class SquareStickAdapter extends RoundStick {
    private SquareStick stick;

    public SquareStickAdapter(SquareStick stick) {
        super(stick.getWidth());
        this.stick = stick;
    }

    public int getRadius() {
        return (int) (stick.getWidth() * Math.sqrt(2) / 2);
    }
}

class AdapterService {
    public void exec() {
        RoundHole hole = new RoundHole(5);
        RoundStick roundStick = new RoundStick(5);
        System.out.println(hole.fits(roundStick)); // Waiting result -> TRUE

        SquareStick smallSquareStick = new SquareStick(5);
        SquareStick largeSquareStick = new SquareStick(10);

        SquareStickAdapter smallSquareStickAdapter = new SquareStickAdapter(smallSquareStick);
        SquareStickAdapter largeSquareStickAdapter = new SquareStickAdapter(largeSquareStick);

        System.out.println(hole.fits(smallSquareStickAdapter)); // TRUE
        System.out.println(hole.fits(largeSquareStickAdapter)); // FALSE

    }
}
public class Application {
    public static void main(String[] args) {
        AdapterService service = new AdapterService();
        service.exec();
    }
}
