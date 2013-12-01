package pl.edu.uj.tcs.memoizer.plugins.impl.mistrzowie;

import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.Meme;
import pl.edu.uj.tcs.memoizer.serialization.IStateObject;

import java.net.URL;
import java.util.Queue;
import java.util.List;
import java.util.LinkedList;

/*
 * 
 */
//public class MistrzowieQueueDownloader implements IDownloadPlugin {
//	
//	/*
//	 * IPlugin members
//	 */
//	private String _serviceName;
//	private IStateObject _state;
//	
//	/*
//	 * IDownloadPlugin members
//	 */
//	private String _workingUrl;
//	private EViewType _view;
//	
//	/*
//	 * Implementation specific members
//	 */
//	private URL _lastSeenUrl;
//	private Queue<Meme> _queue;
//	
//	
//	/*
//	 * Create new queue downloader
//	 */
//	public MistrzowieQueueDownloader(
//			String serviceName, IStateObject state, 
//			EViewType view, String workingUrl) 
//	{
//		_serviceName = serviceName;
//		_state = state;
//		
//		_view = view;
//		_workingUrl = workingUrl;
//		
//		_lastSeenUrl = null;
//		_queue = new LinkedList<Meme>();
//	}
//	
//	/*
//	 * Returns plugin state
//	 */
//	public IStateObject getState() {
//		return _state;
//	}
//	
//	/*
//	 * Returns name of handled service
//	 */
//	public String getServiceName() {
//		return _serviceName; 
//	}
//	
//	/*
//	 * Gets view as plugin work mode
//	 */
//	public EViewType getView(){
//		return _view;
//	}
//	
//	/*
//	 * Check if there is next meme to return
//	 * in current view
//	 */
//	@Override
//	public final boolean hasNext(){
//		return !_queue.isEmpty() || updateWaitingQueue();
//	}
//	
//	/*
//	 * Returns next meme from waiting queue
//	 */
//	@Override
//	public final Meme getNext(){
//		if(hasNext())
//			return _queue.remove();
//		return null;
//	}
//	
//	/*
//	 * Returns next n memes from waiting queue
//	 */
//	@Override
//	public Iterable<Meme> getNext(int n){
//		List<Meme> result = new LinkedList<Meme>();
//		while(n-- > 0 && hasNext()){
//			result.add(getNext());
//		}
//		return result;
//	}
//	
//	private boolean updateWaitingQueue() {
//		List<Meme> newMemes = downloadMemes();
//		for(Meme meme : newMemes)
//			_queue.add(meme);
//		return !_queue.isEmpty();
//	}
//	
//	private List<Meme> downloadMemes() {
//		if(_lastSeenUrl == null)
//			return downloadFirstTime();
//		return downloadNextTime();
//	}
//	
//	private List<Meme> downloadFirstTime() {
//		List<Meme> result = new LinkedList<Meme>();
//		
//		for(int i=10;i>0;++i){
//			List<Meme> pageMemes = downloadMemesFromPageNum(i);
//			for(int j=pageMemes.size()-1;j>=0;--j)
//				result.add(pageMemes.get(j));
//		}
//		
//		if(result.size() > 0){
//			_lastSeenUrl = result.get(result.size() - 1).getImageLink(); 
//		} else {
//			_lastSeenUrl = null;
//		}
//		
//		return result;
//	}
//	
//	private List<Meme> downloadNextTime() {
//		List<Meme> resultReverse = new LinkedList<Meme>();
//		
//		int page = 1, index = -1, tries = 0;
//		while(tries++ < 10 && index == -1) {
//			List<Meme> pageMemes = downloadMemesFromPageNum(page);
//			index = indexContainingSpecificUrl(pageMemes, _lastSeenUrl);
//			
//			if(index == -1)
//				resultReverse.addAll(pageMemes);
//			else for(int i=0;i<index;++i)
//				resultReverse.add(pageMemes.get(i));
//			
//			page++;
//		}
//		
//		List<Meme> result = new LinkedList<Meme>();
//		for(int i=resultReverse.size()-1; i>=0; --i)
//			result.add(resultReverse.get(i));
//		
//		if(result.size() > 0) {
//			_lastSeenUrl = result.get(result.size() - 1).getImageLink();
//		} else {
//			return downloadFirstTime();
//		}
//		
//		return result;
//	}
//	
//	private List<Meme> downloadMemesFromPageNum(int num) {
//		String url = _workingUrl + "/" + num;
//		return MistrzowieMemeDownloader.downloadMemesFromPage(url, _view);
//	}
//	
//	private int indexContainingSpecificUrl(List<Meme> list, URL url) {
//		if(list == null || url == null) return -1;
//		for(int i=0;i<list.size();++i)
//			if(list.get(i).getImageLink().equals(url))
//				return i;
//		return -1;
//	}
//}