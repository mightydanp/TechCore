package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;

public class ProcessingComponent <A extends Material> extends Component<StoneLayerComponent<A>> {
    private final A material;

    public ProcessingComponent(A material) {
        super("processing", "component");
        this.material = material;
    }


    public A end(){
        return material;
    }
}