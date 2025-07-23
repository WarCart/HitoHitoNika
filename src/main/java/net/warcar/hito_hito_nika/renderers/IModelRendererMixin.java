package net.warcar.hito_hito_nika.renderers;


import net.minecraft.util.math.vector.Vector3f;

public interface IModelRendererMixin {
    Vector3f getScale();
    void setXScale(float xScale);
    void setYScale(float yScale);
    void setZScale(float zScale);
    void setScale(Vector3f scale);
}
