package pl.edu.uj.tcs.memoizer.plugins.impl.mistrzowie;

import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IPluginFactory;
import pl.edu.uj.tcs.memoizer.plugins.Meme;
import pl.edu.uj.tcs.memoizer.serialization.IStateObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

/*
 * 
 */
public class MistrzowieSequentialDownloader implements IDownloadPlugin {
	
	/*
	 * IPlugin members
	 */
	private String _serviceName;
	private IStateObject _state;
	private IPluginFactory _pluginFactory;
	
	/*
	 * IDownloadPlugin members
	 */
	private URI _workingUrl;
	private EViewType _view;
	
	/*
	 * Implementation specific members
	 */
	private int _lastSeenPage;
	private URL _lastSeenUrl;
	private Queue<Meme> _queue;
	
	
	/*
	 * Create new sequential downloader
	 */
	public MistrzowieSequentialDownloader(
			String serviceName, IStateObject state, 
			EViewType view, URI workingUrl, IPluginFactory 	pluginFactory) 
	{
		_serviceName = serviceName;
		_state = state;
		
		_view = view;
		_workingUrl = workingUrl;
		
		_lastSeenPage = 0;
		_lastSeenUrl = null;
		_queue = new LinkedList<Meme>();
		_pluginFactory = pluginFactory;
	}
	
	/*
	 * Returns plugin state
	 */
	public IStateObject getState() {
		return _state;
	}
	
	/*
	 * Returns name of handled service
	 */
	public String getServiceName() {
		return _serviceName; 
	}
	
	/*
	 * Gets view as plugin work mode
	 */
	public EViewType getView(){
		return _view;
	}
	
	/*
	 * Check if there is next meme to return
	 * in current view
	 */
	@Override
	public final boolean hasNext(){
		return !_queue.isEmpty() || updateWaitingQueue();
	}
	
	/*
	 * Returns next meme from waiting queue
	 */
	@Override
	public final Meme getNext(){
		if(hasNext())
			return _queue.remove();
		return null;
	}
	
	/*
	 * Returns next n memes from waiting queue
	 */
	@Override
	public Iterable<Meme> getNext(int n){
		List<Meme> result = new LinkedList<Meme>();
		while(n-- > 0 && hasNext()){
			result.add(getNext());
		}
		return result;
	}
	
	private boolean updateWaitingQueue() {
		List<Meme> newMemes = downloadMemes();
		for(Meme meme : newMemes)
			_queue.add(meme);
		return !_queue.isEmpty();
	}
	
	private List<Meme> downloadMemes() {
		if(_lastSeenPage == 0 || _lastSeenUrl == null)
			return downloadFirstTime();
		return downloadNextTime();
	}
	
	private List<Meme> downloadFirstTime() {
		List<Meme> downloadedMemes = downloadMemesFromPageNum(1);
		
		if(downloadedMemes.size() > 0) {
			_lastSeenPage = 1;
			_lastSeenUrl = downloadedMemes.get(downloadedMemes.size() - 1).getImageLink();
		} else {
			_lastSeenPage = 0;
			_lastSeenUrl = null;
		}
		
		return downloadedMemes;
	}
	
	private List<Meme> downloadNextTime() {
		List<Meme> result = new LinkedList<Meme>();
		
		//find last Seen Url
		int page = _lastSeenPage + 1;
		List<Meme> pageMemes = null;
		int index = -1, tries = 0;
		
		//CONSTANT HERE!!!
		while(tries++ < 10 && index == -1) {
			pageMemes = downloadMemesFromPageNum(page);
			index = indexContainingSpecificUrl(pageMemes, _lastSeenUrl);
			page++;
		}
		
		if(index != -1) { //found
			//add from this page
			for(int i = index + 1;  i < pageMemes.size(); ++i)
				result.add(pageMemes.get(i));
			
			//get next page
			List<Meme> nextPageMemes = downloadMemesFromPageNum(page);
			
			if(nextPageMemes.size() > 0) {
				for(Meme meme : nextPageMemes){
					meme.setPluginFactory(this._pluginFactory);
					result.add(meme);
				}
				
				_lastSeenPage = page;
				_lastSeenUrl = result.get(result.size() - 1).getImageLink();
			} else { //next page is empty or got failure
				if(result.size() > 0) {
					_lastSeenPage = page - 1;
					_lastSeenUrl = result.get(result.size() - 1).getImageLink();
				} 
				else return downloadFirstTime();
			}
			
		} else { //not found <- make it a start page for next steps
			result = pageMemes;
			
			if(result.size() > 0) {
				_lastSeenPage = page - 1;
				_lastSeenUrl = result.get(result.size() - 1).getImageLink();
			} 
			else return downloadFirstTime();
		}
		
		return result;
	}
	
	private List<Meme> downloadMemesFromPageNum(int num) {
		try{
			URI uri = new URI(_workingUrl.getScheme(), _workingUrl.getAuthority(), _workingUrl.getPath()+"/"+num, _workingUrl.getQuery(), _workingUrl.getFragment());
			System.out.println("URL: "+uri.toURL());
			
			return MistrzowieMemeDownloader.downloadMemesFromPage(uri.toURL(), _view);
		}catch(MalformedURLException | URISyntaxException e){
			e.printStackTrace();
		}
		return null;
	}
	
	private int indexContainingSpecificUrl(List<Meme> list, URL url) {
		if(list == null || url == null) return -1;
		for(int i=0;i<list.size();++i)
			if(list.get(i).getImageLink().equals(url))
				return i;
		return -1;
	}
}