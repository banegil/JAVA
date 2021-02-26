package simulator.launcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.model.VehicleObserver;
import simulator.model.events.Event;
import simulator.model.exceptions.ContaminationClass;
import simulator.model.exceptions.JunctionList;
import simulator.model.exceptions.NegativeNumber;
import simulator.model.exceptions.NotPendingWaiting;
import simulator.view.MainWindow;

public class Main {

	private static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String mode = "";
	private static Factory<Event> _eventsFactory = null;

	private static void parseArgs(String[] args) {
		Options cmdLineOptions = buildOptions();
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			if(mode.equalsIgnoreCase("console")) parseOutFileOption(line);
			parseStepsOption(line);

			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Modo de ejecucion: gui o console").build());
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Eventos(datos) de archivo de entrada").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Archivo de salida, donde los datos son escritos").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Imprim este mensaje").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("timeslot").hasArg().desc("Tiempo para ejecutar").build());
		
		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}
	
	private static void parseModeOption(CommandLine line) {
			mode = line.getOptionValue("m");
			if(!(mode.equalsIgnoreCase("gui") || mode.equalsIgnoreCase("console")))
				throw new IllegalArgumentException("Debe ser gui o console");
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		if(line.hasOption("i")) {
			_inFile = line.getOptionValue("i");
			if (_inFile == null) {
				throw new ParseException("An events file is missing");
			}
		}
	}
	
	private static void parseStepsOption(CommandLine line) throws ParseException
	{
		if(line.hasOption("t")) {
			String steps = line.getOptionValue("t", Integer.toString(_timeLimitDefaultValue));
			_timeLimitDefaultValue = Integer.decode(steps);
				if(_timeLimitDefaultValue <= 0)
					throw new IllegalArgumentException("Invalid value for steps: " + steps);
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}

	private static void initFactories() {
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add( new RoundRobinStrategyBuilder("round_robin_lss") );
		lsbs.add( new MostCrowdedStrategyBuilder("most_crowded_lss") );
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory
		<>(lsbs);
		
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add( new MoveFirstStrategyBuilder("move_first_dqs") );
		dqbs.add( new MoveAllStrategyBuilder("most_all_dqs") );
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(
		dqbs);
		
		List<Builder<Event>> ebs = new ArrayList<>();
		ebs.add( new NewJunctionEventBuilder("new_junction",lssFactory,dqsFactory) );
		ebs.add( new NewCityRoadEventBuilder("new_city_road") );
		ebs.add( new NewInterCityRoadEventBuilder("new_inter_city_road") );
		ebs.add( new NewVehicleEventBuilder("new_vehicle") );
		ebs.add( new SetWeatherEventBuilder("set_weather") );
		ebs.add( new SetContClassEventBuilder("set_cont_class") );
		_eventsFactory = new BuilderBasedFactory<>(ebs);
	}

	private static void startMode() throws IOException, NegativeNumber, ContaminationClass, JunctionList, NotPendingWaiting {
		TrafficSimulator trafficSimulator = new TrafficSimulator();
		Controller controller = new Controller(trafficSimulator, _eventsFactory);
		if(_inFile != null) {
			InputStream inputStream = new FileInputStream(_inFile);
			controller.loadEvents(inputStream);
		}
		
		if(mode.equalsIgnoreCase("console")) {
			OutputStream outputStream;
			if(_outFile == null)
				outputStream = System.out;
			else
				outputStream = new FileOutputStream(_outFile);
			
			VehicleObserver aobserver = new VehicleObserver(controller);
			controller.run(_timeLimitDefaultValue, outputStream);
			System.out.println(aobserver.toString());
		}
		else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						new MainWindow(controller);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				});

		}
	}

	private static void start(String[] args) throws IOException, NegativeNumber, ContaminationClass, JunctionList, NotPendingWaiting {
		initFactories();
		parseArgs(args);
		startMode();
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
