package visual.map;

import com.jme3.math.*;
import com.jme3.scene.*;
import com.jme3.texture.*;
import com.jme3.texture.plugins.*;
import com.jme3.util.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import javax.imageio.*;

/**
 * Mit flat() und quad() können Flächen erstellt werden
 * Falls die Fläche verkehrtherum ist, inv verwenden
 *
 * Wenn Texturen verwendet werden, muss für jedes flat/quad ein
 * entsprechender Textureneintrag durchgeführt werden
 * Für quad/flat4 sollte texFlat4() verwendet werden
 * Für flat3 kann texFlat3() verwendet werden
 * Andernfalls muss es mit texC() für jeden Punkt einzeln gesetzt werden
 *
 * Am Ende finish() aufrufen
 *
 * Mit outputTextureMap() kann man die TextureMap ansehen
 * Mit textureMap() kann man die TextureMap testen
 */
public class BauMesh1 extends Mesh
{
	//Positionen der Punkte, im Verhältnis zum Ort des Objekts
	private final ArrayList<Vector3f> positions = new ArrayList<>();
	//Lichtreflektionsrichtung, wird pro Punkt gesetzt
	private final ArrayList<Vector3f> normals = new ArrayList<>();
	//Positionen der Punkte auf der TextureMap
	private final ArrayList<Vector2f> texCoord = new ArrayList<>();
	//Die Dreiecke, zeigen auf die Punkte über ihre Nummer
	private final ArrayList<Index3> indexes = new ArrayList<>();

	//Objekt zum Zwischenspeichern von Dreiecksindizes
	private class Index3
	{
		public int[] data;

		public Index3(int i0, int i1, int i2)
		{
			data = new int[]{i0, i1, i2};
		}
	}

	/**
	 * Aufrufen nachdem alle Flächen und Texturkoordinaten definiert sind
	 * Schreibt die Daten in die entsprechenden Buffer
	 */
	protected void finish()
	{
		FloatBuffer positionsBuffer = BufferUtils.createFloatBuffer(positions.toArray(new Vector3f[positions.size()]));
		setBuffer(VertexBuffer.Type.Position, 3, positionsBuffer);
		if(normals.size() == positions.size())
		{
			FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(normals.toArray(new Vector3f[positions.size()]));
			setBuffer(VertexBuffer.Type.Normal, 3, normalBuffer);
		}
		else if(normals.size() != 0)
			throw new RuntimeException("Wenn Normals verwendet wird, " +
					"muss die Liste gleich lang wie die Positionsliste sein");
		if(texCoord.size() == positions.size())
		{
			FloatBuffer texCoordBuffer = BufferUtils.createFloatBuffer(texCoord.toArray(new Vector2f[positions.size()]));
			setBuffer(VertexBuffer.Type.TexCoord, 2, texCoordBuffer);
		}
		else if(texCoord.size() != 0)
			throw new RuntimeException("Wenn TexCoord verwendet wird, " +
					"muss die Liste gleich lang wie die Positionsliste sein");
		int[] indexes2 = new int[indexes.size() * 3];
		for(int i = 0; i < indexes.size(); i++)
			System.arraycopy(indexes.get(i).data, 0, indexes2, i * 3, 3);
		setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indexes2));
		updateBound();
	}

	/**
	 * Erstellt ein Rechteck das normal auf diejenige der drei Achsen ist,
	 * auf der lo und ru den gleichen Wert haben
	 *
	 * @param inv Welche Seite man sieht
	 */
	protected void quad(boolean inv, Vector3f lo, Vector3f ru)
	{
		if(lo.x == ru.x)
			quad(inv, lo, new Vector3f(lo.x, lo.y, ru.z), new Vector3f(ru.x, ru.y, lo.z), ru);
		else if(lo.y == ru.y)
			quad(inv, lo, new Vector3f(ru.x, lo.y, lo.z), new Vector3f(lo.x, ru.y, ru.z), ru);
		else if(lo.z == ru.z)
			quad(inv, lo, new Vector3f(lo.x, ru.y, lo.z), new Vector3f(ru.x, lo.y, ru.z), ru);
		else
			assert false;
	}

	/**
	 * Erstellt ein Viereck
	 *
	 * lo----ro
	 * | \    |
	 * |  \\  |
	 * |    \ |
	 * lu----ru
	 *
	 * @param inv Welche Seite man sieht
	 */
	protected void quad(boolean inv, Vector3f lo, Vector3f ro, Vector3f lu, Vector3f ru)
	{
		flat(inv, lo, ro, ru, lu);
	}

	/**
	 * Erstellt ein eckiges Rohr
	 * mid1 und mid2 müssen in genau einem Wert unterschiedlich sein, dadurch wird die Drehachse bestimmt
	 * start1 und start2 werden jeweils um mid1 und mid2 herumgedreht
	 *
	 * @param inv Welche Seite man sieht
	 * @param edges Anzahl Flächen
	 */
	protected void cylinder(boolean inv, Vector3f mid1, Vector3f start1, Vector3f mid2, Vector3f start2, int edges)
	{
		int axis = -1;
		if(mid1.y == mid2.y && mid1.z == mid2.z)
			axis = 0;
		if(mid1.z == mid2.z && mid1.x == mid2.x)
			axis = 1;
		if(mid1.x == mid2.x && mid1.y == mid2.y)
			axis = 2;
		assert edges > 1 && axis != -1 && !mid1.equals(mid2);
		Vector3f start1a = start1.subtract(mid1);
		Vector3f start2a = start2.subtract(mid2);
		Vector3f[] v1a = new Vector3f[edges + 1];
		Vector3f[] v2a = new Vector3f[edges + 1];
		for(int i = 0; i <= edges; i++)
		{
			float angle = FastMath.TWO_PI * i / edges;
			float sin = FastMath.sin(angle);
			float cos = FastMath.cos(angle);
			switch(axis)
			{
				case 0:
					v1a[i] = new Vector3f(start1a.x, start1a.y * cos - start1a.z * sin, start1a.z * cos + start1a.y * sin).addLocal(mid1);
					v2a[i] = new Vector3f(start2a.x, start2a.y * cos - start2a.z * sin, start2a.z * cos + start2a.y * sin).addLocal(mid2);
					break;
				case 1:
					v1a[i] = new Vector3f(start1a.x * cos + start1a.z * sin, start1a.y, start1a.z * cos - start1a.x * sin).addLocal(mid1);
					v2a[i] = new Vector3f(start2a.x * cos + start2a.z * sin, start2a.y, start2a.z * cos - start2a.x * sin).addLocal(mid2);
					break;
				case 2:
					v1a[i] = new Vector3f(start1a.x * cos - start1a.y * sin, start1a.y * cos + start1a.x * sin, start1a.z).addLocal(mid1);
					v2a[i] = new Vector3f(start2a.x * cos - start2a.y * sin, start2a.y * cos + start2a.x * sin, start2a.z).addLocal(mid2);
					break;
			}
		}
		for(int i = 0; i < edges; i++)
			quad(inv, v1a[i], v1a[i + 1], v2a[i], v2a[i + 1]);
	}

	/**
	 * Erstellt ein Vieleck
	 *
	 *  _- 0 -_   0----1     0
	 * 4  / \  1  | \  |    / \
	 * \ /   \ /  |  \ |   /   \
	 *  3 --- 2   3----2  2-----1
	 *
	 * @param inv Welche Seite man sieht
	 */
	protected void flat(boolean inv, Vector3f... edges)
	{
		assert edges.length >= 3;
		int inv1 = inv ? 1 : 0;
		int start = positions.size();
		Vector3f normal = normal(edges[0], edges[1 + inv1], edges[2 - inv1]);
		for(int i = 0; i < edges.length; i++)
		{
			positions.add(edges[i]);
			normals.add(normal);
		}
		for(int i = 1; i < edges.length - 1; i++)
			indexes.add(new Index3(start, start + i + inv1, start + i + 1 - inv1));
	}

	/**
	 * Erstellt ein regelmäßiges Vieleck mit dem ersten Punkt in der Mitte
	 * normal (die Drehachse) sollte ein Einheitsvektor sein
	 * edg0 wird um mid herumgedreht
	 *
	 * @param inv Welche Seite man sieht
	 * @param edges Anzahl Kanten/Flächen
	 */
	protected void circle(boolean inv, Vector3f normal, Vector3f mid, Vector3f edg0, int edges)
	{
		assert edges >= 3;
		int inv1 = inv ? 1 : 0;
		int start = positions.size();
		Vector3f edg0a = edg0.subtract(mid);
		Vector3f edg0n = edg0a.cross(normal);
		positions.add(mid);
		normals.add(normal);
		for(int i = 0; i < edges; i++)
		{
			float angle = FastMath.TWO_PI * i / edges;
			float sin = FastMath.sin(angle);
			float cos = FastMath.cos(angle);
			positions.add(mid.add(edg0a.mult(cos)).subtract(edg0n.mult(sin)));
			normals.add(normal);
		}
		for(int i = 0; i < edges; i++)
			indexes.add(new Index3(start, start + 1 + (i + inv1) % edges, start + 1 + (i + 1 - inv1) % edges));
	}

	//Ermittelt die Lichtreflektionsrichtung
	private Vector3f normal(Vector3f edg0, Vector3f edg1, Vector3f edg2)
	{
		Vector3f diff1 = edg1.subtract(edg0);
		Vector3f diff2 = edg2.subtract(edg0);
		return diff1.cross(diff2).normalizeLocal();
	}

	/**
	 * Trägt einen Eintrag in TexCoord ein
	 */
	protected void texC(float x0, float y0)
	{
		texCoord.add(new Vector2f(x0, y0));
	}

	/**
	 * Trägt ein Rechteck in TexCoord ein (4 Einträge)
	 *
	 * 0,0----1,0
	 *  | \    |
	 *  |  \\  |
	 *  |    \ |
	 * 0,1----1,1
	 */
	protected void texFlat4(float x0, float y0, float x1, float y1)
	{
		texFlat4(x0, y0, x1, y1, 0);
	}

	/**
	 * Trägt ein Rechteck in TexCoord ein (4 Einträge)
	 *
	 * @param spin Drehung der Textur
	 */
	protected void texFlat4(float x0, float y0, float x1, float y1, int spin)
	{
		Vector2f[] ar = new Vector2f[]{new Vector2f(x0, y0), new Vector2f(x1, y0),
				new Vector2f(x1, y1), new Vector2f(x0, y1)};
		for(int i = spin; i < spin + 4; i++)
			texCoord.add(ar[i % 4]);
	}

	/**
	 * Trägt für cylinder Einträge in TexCoord ein (anz*4 Einträge)
	 * Einträge nach dem ersten werden direkt rechts neben dem vorherigen platziert
	 */
	protected void texCylinder(float x0, float y0, float x1, float y1, int anz)
	{
		for(int i = 0; i < anz; i++)
		{
			float xp = (x1 - x0) * i;
			texFlat4(x0 + xp, y0, x1 + xp, y1);
		}
	}

	/**
	 * Trägt für circle Einträge in TexCoord ein (edges+1 Einträge)
	 * Der erste Eintrag ist (xm, ym), die anderen werden um (xm, ym) herumgedreht
	 */
	protected void texCircle(float xm, float ym, float x0, float y0, int edges)
	{
		texCoord.add(new Vector2f(xm, ym));
		float xd = x0 - xm;
		float yd = y0 - ym;
		for(int i = 0; i < edges; i++)
		{
			float angle = FastMath.TWO_PI * i / edges;
			float sin = FastMath.sin(angle);
			float cos = FastMath.cos(angle);
			texCoord.add(new Vector2f(xm + xd * cos - yd * sin, ym + yd * cos + xd * sin));
		}
	}

	/**
	 * Erstellt eine Datei mit einer TextureMap
	 * @param numh Die fontsize der Nummerierung
	 *             0 für keine Nummerierung
	 */
	public void outputTextureMap(int w, int h, int numh)
	{
		try
		{
			ImageIO.write(textureMapImage(w, h, numh), "png", new File("TexMap_" + getClass().getSimpleName() + ".png"));
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return Eine TextureMap als jme-Textur
	 * @param numh Die fontsize der Nummerierung
	 *             0 für keine Nummerierung
	 */
	public Texture textureMap(int w, int h, int numh)
	{
		return new Texture2D(new AWTLoader().load(textureMapImage(w, h, numh), false));
	}

	/**
	 * @return Eine TextureMap als BufferedImage
	 * @param numh Die fontsize der Nummerierung
	 *             0 für keine Nummerierung
	 */
	public BufferedImage textureMapImage(int w, int h, int numh)
	{
		BufferedImage img1 = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gd = img1.createGraphics();
		gd.setColor(Color.BLACK);
		gd.fillRect(0, 0, w, h);
		gd.setColor(new Color(0, 255, 0, 40));
		int[] xe = new int[3];
		int[] ye = new int[3];
		for(Index3 index3 : indexes)
		{
			for(int j = 0; j < 3; j++)
			{
				xe[j] = (int)(texCoord.get(index3.data[j]).getX() * w);
				ye[j] = (int)(texCoord.get(index3.data[j]).getY() * h);
			}
			gd.fillPolygon(xe, ye, 3);
			gd.drawPolygon(xe, ye, 3);
		}
		if(numh > 0 && indexes.size() > 0)
		{
			gd.setColor(Color.WHITE);
			gd.setFont(new Font(null, Font.PLAIN, numh));
			FontMetrics fm = gd.getFontMetrics();
			HashMap<Integer, Vector2f> last = new HashMap<>();
			int counter = 0;
			indexTxcFill(last, indexes.get(0));
			for(int i = 1; i < indexes.size(); i++)
			{
				if(indexTxcCheck(last, indexes.get(i)))
				{
					drawNum(last, gd, fm, String.valueOf(counter), w, h);
					last.clear();
					counter++;
				}
				indexTxcFill(last, indexes.get(i));
			}
			drawNum(last, gd, fm, String.valueOf(counter), w, h);
		}
		return img1;
	}

	private void indexTxcFill(HashMap<Integer, Vector2f> last, Index3 index3)
	{
		for(int i = 0; i < 3; i++)
			last.put(index3.data[i], texCoord.get(index3.data[i]));
	}

	private boolean indexTxcCheck(HashMap<Integer, Vector2f> last, Index3 index3)
	{
		for(int i = 0; i < 3; i++)
			if(last.containsKey(index3.data[i]))
				return false;
		return true;
	}

	//Platziert eine Zahl in der Mitte einer Fläche
	private void drawNum(HashMap<Integer, Vector2f> last, Graphics2D gd, FontMetrics fm, String t, int w, int h)
	{
		Vector2f xy = last.values().stream().reduce(new Vector2f(0, 0), Vector2f::addLocal).divide(last.size());
		gd.drawString(t, (int)(xy.x * w) - fm.stringWidth(t) / 2, (int)(xy.y * h) + fm.getHeight() / 2);
	}
}