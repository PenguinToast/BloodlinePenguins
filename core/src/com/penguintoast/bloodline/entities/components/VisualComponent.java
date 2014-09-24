package com.penguintoast.bloodline.entities.components;

import com.badlogic.ashley.core.Component;
import com.penguintoast.bloodline.actors.vfx.VisualEffect;

/**
 * A component that specifies that this entity should be drawn
 */
public class VisualComponent extends Component {
    
    /**
     * Constructs a VisualComponent with the specified visual
     * 
     * @param visual The visual to render 
     */
    public VisualComponent(VisualEffect visual) {
        this.visual = visual;
    }
    
    /**
     * The visual effect to render
     */
    public VisualEffect visual;
}
