package ru.smartsolutions.mesgi.dumptable;

import java.io.IOException;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component(name = "DumtableService")
public class Dumptable {
	
	
//	public static void main (String[] args){
//		getDumptable();
//	}
	
	@SuppressWarnings("unused")
	private BundleContext context;
	
	private NodeScanner scanner;
	
//	private IDiodService diodService;
//	
//	@Reference(
//            name = "IDiodService",
//            service = IDiodService.class,
//            cardinality = ReferenceCardinality.MANDATORY,
//            policy = ReferencePolicy.STATIC,
//            unbind = "unbindDiodService"
//    )
//	private void bindDiodService(IDiodService diodService){
//		System.out.println("bindDiod");
//		this.diodService = diodService;
//	}
//	
//	@SuppressWarnings("unused")
//	private void unbindDiodService(IDiodService diodService){
//		System.out.println("unbindDiod");
//		if(this.diodService == diodService)
//			this.diodService = null;
//	}
	
	@Activate
	public void start(BundleContext context){
		this.context = context;
		System.out.println("Started dumptable service");
//		scanner = new NodeScanner();
		
//		Thread thr = new Thread(scanner);
//		thr.setDaemon(true);
//		thr.start();
	}
	
	@Deactivate
	public void stop(BundleContext context){
		context = null;
//		scanner.stop();
		System.out.println("Stoped dumptable service");
	}

	private static void getDumptable() {
		
		String bencoded = "d0000005:counti1e11:deprecation36:ip,path,version will soon be removed5:peersi1e12:routingTableld4:addr78:v15.0000.0000.0000.0001.pc5n0m6lnf2gpgkffg8f69rzx71076psfbhjsku3xskt0q36jl60.k2:ip39:fcfb:dfed:56da:82d5:6508:eadc:6b5f:03e74:linki4294967295e4:path19:0000.0000.0000.00014:timei2454687e7:versioni15eed4:addr78:v15.0000.0000.0000.0013.9mxsbwg4ytjdh32ujdfxhmg511svv42363v28pv4cpgbq651u200.k2:ip39:fcec:16da:6714:7381:12aa:6db9:b10f:2fe74:linki729378252e4:path19:0000.0000.0000.00134:timei1242e7:versioni15eeee";
		
		AdminAPI api = new AdminAPI(null, 0, null);
		
		Map<String, Object> response = null;
		try {
			response = api.parse(bencoded);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}























































