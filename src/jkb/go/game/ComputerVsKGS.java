/**
 * 
 */
package jkb.go.game;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

import com.gokgs.client.gtp.GtpClient;
import com.gokgs.client.gtp.Options;

/**
 * @author joey
 *
 */
public class ComputerVsKGS
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		InputStream in = System.in;
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		OutputStream out = System.out;
		BufferedOutputStream bout = new BufferedOutputStream(out);
        Options options = null;
        Properties props = new Properties();
        String logName = "jkb";
		
        try
		{
			props.load(ComputerVsKGS.class.getResourceAsStream("jkb/go/gtp/kgs/prop/gtpKgs.properties"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		options = new Options(props, logName);
		
		GtpClient client = new GtpClient(in, out, options);
		client.go();
		
		try
		{
			while (true)
			{
				String line = null;
				while ((line=br.readLine())!=null)
				{
					System.out.println(line);
				}
				
				System.out.println();
				System.out.print("Respond: ");
				String command = br.readLine();
				System.out.println();
				out.write(command.getBytes());
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
