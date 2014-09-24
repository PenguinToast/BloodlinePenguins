package com.penguintoast.bloodline.entities;

import com.badlogic.ashley.core.Entity;
import com.penguintoast.bloodline.entities.components.PositionComponent;
import com.penguintoast.bloodline.entities.components.VisualComponent;

public class BloodlineEntity extends Entity {

    public BloodlineEntity() {
        add(new PositionComponent());
        add(new VisualComponent(null));
    }

}
