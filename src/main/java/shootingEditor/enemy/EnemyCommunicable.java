package shootingEditor.enemy;

import shootingEditor.vector.Int2Vector;

public interface EnemyCommunicable {
	
	Enemy getGeneratingChild (Enemy parent);
	void generateExplosiveObject (Enemy parent);
	Int2Vector getMyPlanePos();
	void setMyPlanePos(Int2Vector myPlanePos);
}
