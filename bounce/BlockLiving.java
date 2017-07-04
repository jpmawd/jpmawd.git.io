package bounce;

import java.awt.Color;

public abstract class BlockLiving extends Block{
	
	public int health, maxHealth;
	private String effectOnDestruction = "CRUMBLE";
	private Color effectColor = Color.BLACK;

	
	public BlockLiving(int xPos, int yPos, int maxHealth, String effectOnDestruction) {
		super(xPos, yPos);
		this.effectOnDestruction = effectOnDestruction;
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}
	

	public void procedure() {
		if(health < 1) {
			isTerminated = true;
		}
		else if(random.nextInt(100) == 0 && health < maxHealth / 2) {
			Main.ps.add(new ParticleSystem(ParticleSystem.generateRandomParticles(xCenter, yPos, 10, 30 * health / maxHealth), (ParticleSystem.generateRandomForces(xCenter, yCenter + 20, .1, 5, 3))));
		}
		if(isTerminated) {
			onTermination();
		}
	}
	
	public void onTermination() {
		if(effectOnDestruction != null) { 
			Main.gfx.add(GraphicEffect.createGraphicEffectByName(effectOnDestruction, xPos, yPos, effectColor));
		}
	}


	public String getEffectOnDestruction() {
		return effectOnDestruction;
	}


	public void setEffectOnDestruction(String effectOnDestruction) {
		this.effectOnDestruction = effectOnDestruction;
	}


	public Color getEffectColor() {
		return effectColor;
	}


	public void setEffectColor(Color effectColor) {
		this.effectColor = effectColor;
	}
	
}
