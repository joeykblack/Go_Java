/**
 * 
 */
package jkb.go.helper.uct;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jkb.go.model.Type;
import jkb.go.model.boardstate.BoardStateBitMaps;
import jkb.go.model.uct.Node;

/**
 * @author joey
 *
 */
public class UCTUtilThreaded
{
	
	public static Node UCTSearch(BoardStateBitMaps bs, Type type, int depth)
	{
		Node root = new Node(-1, -1);
		ExecutorService pool = Executors.newFixedThreadPool(8);
		
		for (int i = 0; i < depth; i++)
		{
			pool.execute(new UCTSimulationTask(root, bs, type));
		}
		
		try
		{
			pool.shutdown();
			if (!pool.awaitTermination(20, TimeUnit.SECONDS))
			{
				System.out.println("Threads did not end after 20 seconds. Forcing move...");
				pool.shutdownNow();
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		return root.best;
	}

}
