package sincity.model;

import sincity.view.Renderer;
import sincity.view.TrafficLightsDisplay;

import java.util.Observable;


public abstract class TrafficLights extends Observable {
    LightColor currentColor;
    Direction[] lightDirection;
    Direction lightsOrienttion;
    //all traffic lights  created green by default

    public TrafficLights() {
        this.currentColor = LightColor.GREEN;

    }

    public LightColor getCurrentColor() {
        return this.currentColor;
    }

    public Direction[] getLightDirection() {
        return this.lightDirection;
    }


    public void changeColor(LightColor lightColor) {
        synchronized (this) {
            currentColor = lightColor;
        }
        setChanged();
        notifyObservers(currentColor);
    }

}
