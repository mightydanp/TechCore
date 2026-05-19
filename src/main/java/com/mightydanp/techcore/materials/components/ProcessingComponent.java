package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.world.item.properties.ProcessedStage;

public class ProcessingComponent <A extends Material> extends Component<StoneLayerComponent<A>> {
    private final A material;

    private ProcessedStage processedStage;

    public ProcessingComponent(A material) {
        super("processing", "component");
        this.material = material;
    }

    public ProcessingComponent<A> setProcessedStage(ProcessedStage processedStage) {
        this.processedStage = processedStage;
        return this;
    }

    public A end(){
        return material;
    }
}