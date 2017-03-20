package cn.nulladev.railguncraft.client.renderer;

import java.util.Random;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.nulladev.railguncraft.entity.EntityEMPShield;

public class RendererEMPShield extends Render {

	public RendererEMPShield(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityEMPShield)entity, x, y, z, p_76986_8_, p_76986_9_);
    }
	
	public void doRender(EntityEMPShield entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);

		GlStateManager.disableTexture2D();
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);;  
        GlStateManager.depthMask(false);
        
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();
        float rand = 0.25F * new Random().nextFloat();
        
        GlStateManager.color(0, 0, 0, rand);
                
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos( 0.5D, 0D,  0.5D).color(0, 0, 0, rand).endVertex();
        worldrenderer.pos( 0.5D, 0D, -0.5D).color(0, 0, 0, rand).endVertex();
        worldrenderer.pos(-0.5D, 0D, -0.5D).color(0, 0, 0, rand).endVertex();
        worldrenderer.pos(-0.5D, 0D,  0.5D).color(0, 0, 0, rand).endVertex();
        tessellator.draw();
        
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos( 0.5D, 2D,  0.5D).color(0, 0, 0, rand).endVertex();
        worldrenderer.pos( 0.5D, 2D, -0.5D).color(0, 0, 0, rand).endVertex();
        worldrenderer.pos(-0.5D, 2D, -0.5D).color(0, 0, 0, rand).endVertex();
        worldrenderer.pos(-0.5D, 2D,  0.5D).color(0, 0, 0, rand).endVertex();
        tessellator.draw();
        
        for (int i = 0; i < 4; ++i) {
        	GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, 1.0F);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos( 0.5D, 0D, 0.5D).color(0, 0, 0, rand).endVertex(); // 右下
            worldrenderer.pos( 0.5D, 2D, 0.5D).color(0, 0, 0, rand).endVertex(); // 右上
            worldrenderer.pos(-0.5D, 2D, 0.5D).color(0, 0, 0, rand).endVertex(); // 左上
            worldrenderer.pos(-0.5D, 0D, 0.5D).color(0, 0, 0, rand).endVertex(); // 左下
            tessellator.draw();
        }

        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();

        GlStateManager.popMatrix();

    }

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return null;
	}
}