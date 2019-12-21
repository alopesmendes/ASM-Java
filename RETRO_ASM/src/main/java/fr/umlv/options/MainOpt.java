package fr.umlv.options;


import java.util.ArrayList;
import java.util.List;

public class MainOpt {

	public static void main(String[] args) {
		List<Option>options = new ArrayList<Option>();
		int i = 0;
		String arg;
		while (i < args.length ) {
            arg = args[i];
            switch (arg) {
            	case "-Target": options.add(new OptTarget(i, args));break;
            	case "-Features": options.add(new OptFeatures(i, args));break;
            	case "-Infos" : options.add(new OptInfo());break;
            	default: break;
            }
            i++;
		}
		System.out.println(options.size());
		options.forEach(x-> x.execute());
            

	}

}
