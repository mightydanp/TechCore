package com.mightydanp.techcore.api.resources;

import net.minecraftforge.client.event.RegisterColorHandlersEvent;

public interface BaseRegistries<A> {
    @SuppressWarnings("unchecked")
    default A init(){
        return (A) this;
    };
    @SuppressWarnings("unchecked")
    default A initClient(){
        return (A) this;
    };

    @SuppressWarnings("unchecked")
    default A initLanguages(){
        return (A) this;
    };

    default A initItemProperties(){
        return (A) this;
    }

    @SuppressWarnings("unchecked")
    default A initBlockProperties(RegisterColorHandlersEvent.Block event){
        return (A) this;
    }
    @SuppressWarnings("unchecked")
    default A initClientRenderLayers(RegisterColorHandlersEvent.Item event){
        return (A) this;
    }
}
