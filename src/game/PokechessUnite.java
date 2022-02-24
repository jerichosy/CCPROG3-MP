package game;

/**
 * CCPROG3 MP AY2022T1 - Pokechess UNITE
 * Specs: https://docs.google.com/document/d/1PY8cfhnJTiySyXe070OLo6sVTK8MWT4qT8OjZfDIUyU/
 *
 * @author HIDALGO, FRANCISCO JOSE NANZAN
 * @author SY, MATTHEW JERICHO GO
 */
public class PokechessUnite {
	public static void main(String[] args) {
		View view = new View();
		Game game = new Game();
		Controller controller = new Controller(view, game);
		//view.setVisible(true);  // already in View class
	}
}
