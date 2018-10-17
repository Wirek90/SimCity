package sincity.model;

import javafx.animation.PathTransition;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import sincity.view.Renderer;
import sincity.view.VehicleDisplay;

import java.util.HashMap;


class Vehicle {
    double maxSpeed;
    double speed;
    double size;
    private RoadPuzzle currentRoadPuzzle;
    private Direction arrivalDirection;
    private Direction outDirection;
    private City city;
    private Renderer renderer;
    private VehicleDisplay vehicleDisplay;

    Vehicle(City city, Renderer renderer, RoadPuzzle roadPuzzle, Direction arrivalDirection) {
        this.renderer = renderer;
        this.currentRoadPuzzle = roadPuzzle;
        this.arrivalDirection = arrivalDirection;
        this.vehicleDisplay = renderer.renderVehicle();
        this.city = city;
        move();
    }

    private void move() {
        outDirection = getRandomOutDirection(currentRoadPuzzle.getRoadDirections());
        String fromTo = arrivalDirection.toString() + "_" + outDirection.toString();
        PathToMove pathToMove = new PathToMove(currentRoadPuzzle, fromTo);
        PathTransition pathTransition = renderer.moveAnimation(vehicleDisplay, pathToMove);
        pathTransition.setOnFinished(event -> {
            changeRoadPuzzle(currentRoadPuzzle);
            if (currentRoadPuzzle != null) {
                move();
            }
        });
    }

    private Direction getRandomOutDirection(HashMap<Direction, Boolean> possibleDirections) {
        int randomIndex;
        boolean isChosenDirection;
        Direction chosen;
        do {
            randomIndex = (int) Math.floor(Math.random() * 4);  // number of directions
            chosen = Direction.values()[randomIndex];
            isChosenDirection = possibleDirections.get(chosen);
        } while (!isChosenDirection || Direction.values()[randomIndex].equals(arrivalDirection));
        return chosen;
    }


    private void changeRoadPuzzle(RoadPuzzle puzzle) {
        currentRoadPuzzle = findNextPuzzle(puzzle, outDirection);
    }

    private RoadPuzzle findNextPuzzle(RoadPuzzle puzzle, Direction outDir) {
        int currentX = puzzle.getIndexX();
        int currentY = puzzle.getIndexY();

        int nextPuzzleIndexX = currentX;
        int nextPuzzleIndexY = currentY;

        switch (outDir) {
            case W:
                nextPuzzleIndexX = currentX - 1;
                arrivalDirection = Direction.E;
                break;
            case E:
                nextPuzzleIndexX = currentX + 1;
                arrivalDirection = Direction.W;
                break;
            case N:
                nextPuzzleIndexY = currentY - 1;
                arrivalDirection = Direction.S;
                break;
            case S:
                nextPuzzleIndexY = currentY + 1;
                arrivalDirection = Direction.N;
                break;
        }

        try {
            return city.getPuzzleBoard()[nextPuzzleIndexX][nextPuzzleIndexY];
        } catch (Exception e) {
            return null;
        }
    }
}
