package org.peigenyou.hw9;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.peigenyou.hw2_5.StateChangerImpl;
import org.peigenyou.hw2_5.StateExplorerImpl;
import org.shared.chess.Color;
import org.shared.chess.Move;
import org.shared.chess.State;
import org.shared.chess.StateChanger;
import org.shared.chess.StateExplorer;

import com.google.gwt.user.client.rpc.core.java.util.Collections;

public class HeuristicImpl implements Heuristic{
	private StateExplorer stateExplorer = new StateExplorerImpl();
	private StateChanger stateChanger=new StateChangerImpl();
	@Override
	public int getStateValue(State state) {
		if (state.getGameResult() != null) {
      if (state.getGameResult().getWinner() == Color.WHITE)    return 2000000;
      if (state.getGameResult().getWinner() == Color.BLACK)    return -2000000;
		}
		int sum=0;
		for(int row=0;row<8;++row){
			for(int col=0; col<8;++col){
				int pv=PieceValue.getPieceValue(state.getPiece(row,col), row, col);
				//System.out.println(pv);
				sum+=pv;
			}
		}
		return sum;
	}

	@Override
	public Iterable<Move> getOrderedMoves(State state) {
		StateExplorer stateExplorer=new StateExplorerImpl();
		Set<Move> moves=stateExplorer.getPossibleMoves(state);
		List<Move> result=new LinkedList<Move>();
		List<MoveValue> cache=new LinkedList<MoveValue>();
		int sum=0;
		for (Move move : moves) {
      State newState =state.copy(); 
      stateChanger.makeMove(newState, move);
      //System.out.println(org.peigenyou.hw3.Presenter.stateToString(newState));
      int value = getStateValue(newState);
      //System.out.println(value);
      sum += value;
      cache.add(new MoveValue(move, value));
		}
		MoveValue[] heap=new MoveValue[cache.size()];
		for(int i=0;i<cache.size();++i){
			heap[i]=cache.get(i);	
		}		
		//System.out.println(cache.size());
		if(cache.size()==0) return null;
		quickSort(heap, 0,heap.length-1);
		for(int i=0;i<heap.length;++i){
			result.add(heap[i].move);
		}
		return result;
	}
	int partition(MoveValue arr[], int left, int right)
	{
				
	      int i = left, j = right;
	      MoveValue tmp;
	      int ii=(int)((left + right) / 2);
	      //System.out.println(ii);
	      int pivot = arr[ii].value;
	      while (i <= j) {
	           while (arr[i].value < pivot)
	                 i++;
	            while (arr[j].value > pivot)
	                  j--;
	            if (i <= j) {
	                  tmp = arr[i];
	                  arr[i] = arr[j];
	                  arr[j] = tmp;
	                  i++;
	                  j--;
	            }
	      };
	      return i;
	}

	void quickSort(MoveValue arr[], int left, int right) {
	      int index = partition(arr, left, right);
	      if (left < index - 1)
	            quickSort(arr, left, index - 1);
	      if (index < right)
	            quickSort(arr, index, right);
	}
	class MoveValue implements Comparable<MoveValue>{
		Move move;
		int value;
		public MoveValue(Move move,int value){
			this.move=move;
			this.value=value;
		}
		@Override
		public int compareTo(MoveValue c) {
			
			if(this.value<c.value) return -1;
			else if(this.value>c.value) return 1;
			else 	return 0;
		}
	}

}
