package pl.edu.uj.tcs.memoizer.plugins.impl.mistrzowie;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginFactory;
import pl.edu.uj.tcs.memoizer.plugins.InvalidViewException;
import pl.edu.uj.tcs.memoizer.serialization.IStateObject;

/**
 * mistrzowie.org download plugin factory implementation
 * @author pmikos (sokar92)
 */
public class MistrzowieDownloadPluginFactory implements IPluginFactory {

	@Override
	public List<EViewType> getAvailableDownloadViews() {
		List<EViewType> list = new ArrayList<EViewType>();
		list.add(EViewType.CHRONOLOGICAL);
		list.add(EViewType.FAVOURITE);
		list.add(EViewType.UNSEEN);
		list.add(EViewType.QUEUE);
		list.add(EViewType.SEARCH);
		list.add(EViewType.RANDOM);
		return list;
	}

	/**
	 * @author pkubiak
	 */
	@Override
	public Image getIcon() {
		try{
			byte T[] = new byte[]{
				(byte)0x47,(byte)0x49,(byte)0x46,(byte)0x38,(byte)0x39,(byte)0x61,(byte)0x10,(byte)0x00,
				(byte)0x10,(byte)0x00,(byte)0xf4,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
				(byte)0x09,(byte)0x09,(byte)0x09,(byte)0x13,(byte)0x13,(byte)0x13,(byte)0x1b,(byte)0x1b,
				(byte)0x1b,(byte)0x23,(byte)0x23,(byte)0x23,(byte)0x2a,(byte)0x2a,(byte)0x2a,(byte)0x33,
				(byte)0x33,(byte)0x33,(byte)0x3b,(byte)0x3b,(byte)0x3b,(byte)0x41,(byte)0x41,(byte)0x41,
				(byte)0x59,(byte)0x59,(byte)0x59,(byte)0x61,(byte)0x61,(byte)0x61,(byte)0x74,(byte)0x74,
				(byte)0x74,(byte)0x99,(byte)0x99,(byte)0x99,(byte)0xaa,(byte)0xaa,(byte)0xaa,(byte)0xca,
				(byte)0xca,(byte)0xca,(byte)0xd4,(byte)0xd4,(byte)0xd4,(byte)0xd9,(byte)0xd9,(byte)0xd9,
				(byte)0xe3,(byte)0xe3,(byte)0xe3,(byte)0xec,(byte)0xec,(byte)0xec,(byte)0xf5,(byte)0xf5,
				(byte)0xf5,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
				(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
				(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
				(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
				(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x21,(byte)0xf9,(byte)0x04,
				(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x2c,(byte)0x00,(byte)0x00,
				(byte)0x00,(byte)0x00,(byte)0x10,(byte)0x00,(byte)0x10,(byte)0x00,(byte)0x00,(byte)0x05,
				(byte)0x57,(byte)0x20,(byte)0x20,(byte)0x8e,(byte)0x64,(byte)0x69,(byte)0x52,(byte)0x8f,
				(byte)0x41,(byte)0x51,(byte)0x93,(byte)0x39,(byte)0x06,(byte)0x54,(byte)0x83,(byte)0x44,
				(byte)0x2b,(byte)0xe5,(byte)0x02,(byte)0x05,(byte)0xc5,(byte)0x08,(byte)0x45,(byte)0x6e,
				(byte)0xbb,(byte)0x07,(byte)0xca,(byte)0x42,(byte)0x40,(byte)0x28,(byte)0x48,(byte)0x7e,
				(byte)0xa6,(byte)0x04,(byte)0x45,(byte)0x11,(byte)0x08,(byte)0x08,(byte)0x26,(byte)0x94,
				(byte)0x80,(byte)0x6b,(byte)0x41,(byte)0x41,(byte)0x88,(byte)0x02,(byte)0xd0,(byte)0x5b,
				(byte)0x83,(byte)0x32,(byte)0x18,(byte)0xad,(byte)0xb4,(byte)0x5c,(byte)0x2f,(byte)0xb2,
				(byte)0x04,(byte)0x89,(byte)0x8a,(byte)0x6f,(byte)0x65,(byte)0xa9,(byte)0xe8,(byte)0xeb,
				(byte)0x82,(byte)0xaa,(byte)0xb7,(byte)0x63,(byte)0x52,(byte)0xb6,(byte)0x16,(byte)0xbb,
				(byte)0xad,(byte)0x60,(byte)0x2b,(byte)0x87,(byte)0xe1,(byte)0x66,(byte)0x38,(byte)0xe0,
				(byte)0x0a,(byte)0x07,(byte)0x06,(byte)0x02,(byte)0x37,(byte)0x84,(byte)0x23,(byte)0x21,
				(byte)0x00,(byte)0x3b
			};

			return ImageIO.read(new ByteArrayInputStream(T)); 
		}catch(IOException e){
			return null;
		}
	}

	@Override
	public String getServiceName() {
		return "Mistrzowie";
	}

	@Override
	public IDownloadPlugin newInstance(IStateObject state, EViewType viewType)throws InvalidViewException {
		try{
			switch(viewType){
			
				case CHRONOLOGICAL:
				case UNSEEN:
					return new MistrzowieSequentialDownloader("MistrzowieChrono", state, viewType, new URI("http://www.mistrzowie.org/page"), this);
				case QUEUE:
					return new MistrzowieSequentialDownloader("MistrzowieQueue", state, viewType, new URI("http://www.mistrzowie.org/poczekalnia/page"), this);
				case FAVOURITE:
					return new MistrzowieSequentialDownloader("MistrzowieTopPercent", state, viewType, new URI("http://www.mistrzowie.org/topka/procenty/page"), this);
				case SEARCH:
					throw new RuntimeException("Missing keyword for Searching");
				case RANDOM:
					return new MistrzowieSingleDownloader("MistrzowieRandom", state, viewType, new URI("http://www.mistrzowie.org/losuj"), this);
				//case UNSEEN:
					//return new MistrzowieQueueDownloader("MistrzowieUnseen", state, view, "http://www.mistrzowie.org/page");
				default:
					throw new InvalidViewException();	
			}
		}catch(URISyntaxException e){
			throw new InvalidViewException();
		}
	}

	/**
	 * Extended version of newInstance taking additional parameter for parametrised views.
	 * @author pkubiak
	 */
	@Override
	public IDownloadPlugin newInstance(IStateObject state, EViewType viewType, Object parameters) throws InvalidViewException {
		switch(viewType){
//			case SEARCH:
//				String searchKey = (String)parameters;
//				try {
//					String url = "http://www.mistrzowie.org/szukaj/page?q=" + URLEncoder.encode(searchKey, "UTF-8");
//					URI uri = new URI(url);
//					return new DemotySequentialDownloader("MistrzowieSearch", state, viewType, uri, this);
//				} catch (UnsupportedEncodingException | URISyntaxException e) {
//					e.printStackTrace();
//					throw new InvalidViewException();
//				}
			default:
				return newInstance(state, viewType);
		}
	}
	
}
