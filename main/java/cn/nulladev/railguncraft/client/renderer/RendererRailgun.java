package cn.nulladev.railguncraft.client.renderer;

import org.lwjgl.opengl.GL11;

import cn.nulladev.railguncraft.entity.EntityRailgun;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class RendererRailgun extends Render {

	public RendererRailgun(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityRailgun)entity, x, y, z, p_76986_8_, p_76986_9_);
    }
	
	public void doRender(EntityRailgun entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {		
		GlStateManager.pushMatrix();
        Vec3d origPos = entity.getOrigPos();
        GlStateManager.translate(x - entity.posX + origPos.xCoord, y - entity.posY + origPos.yCoord, z - entity.posZ + origPos.zCoord);
        GlStateManager.rotate(entity.prevRotationYaw - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 772);  
        GlStateManager.depthMask(false);
                
        //GlStateManager.color(1F, 1F, 0.5F, 0.5F);
            
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();
		
        double length = (int) Math.sqrt((entity.posX - origPos.xCoord) * (entity.posX - origPos.xCoord) 
        		+ (entity.posY - origPos.zCoord) * (entity.posY - origPos.zCoord) 
        		+ (entity.posZ - origPos.zCoord) * (entity.posZ - origPos.zCoord));
        for (int i = 0; i < 4; ++i) {
        	GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, 1.0F);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            
            if (entity.isAdvanced) {
	            worldrenderer.pos(0.0D, -0.2D, 0.0D).color(1F, 1F, 0F, 1F).endVertex();
	            worldrenderer.pos(length, -0.2D, 0.0D).color(1F, 1F, 0F, 1F).endVertex();
	            worldrenderer.pos(length, 0.2D, 0.0D).color(1F, 1F, 0F, 1F).endVertex();
	            worldrenderer.pos(0.0D, 0.2D, 0.0D).color(1F, 1F, 0F, 1F).endVertex();
            } else {
            	worldrenderer.pos(0.0D, -0.2D, 0.0D).color(1F, 1F, 0.5F, 0.5F).endVertex();
	            worldrenderer.pos(length, -0.2D, 0.0D).color(1F, 1F, 0.5F, 0.5F).endVertex();
	            worldrenderer.pos(length, 0.2D, 0.0D).color(1F, 1F, 0.5F, 0.5F).endVertex();
	            worldrenderer.pos(0.0D, 0.2D, 0.0D).color(1F, 1F, 0.5F, 0.5F).endVertex();
            }

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

