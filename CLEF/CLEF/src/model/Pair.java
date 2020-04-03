package model;


//User defined Pair class 
public class Pair<T, P> { 
		public T x; 
		public P y; 

		// Constructor 
		public Pair(T x, P y) 
		{ 
			this.x = x; 
			this.y = y; 
		} 

		@Override
		public boolean equals(Object obj) {
			Pair b = (Pair)obj;
			return this.x.equals(b.x) && this.y.equals(b.y);
		}
	} 
