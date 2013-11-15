package mori.go;

import java.lang.Cloneable;

public class Position
{
	public int x;
	public int y;
	
	public Position( Position p){
		this.x = p.x;
		this.y = p.y;
	}

	public Position(){
	}

	public Position( int x, int y){
		this.x = x;
		this.y = y;
	}

	public boolean equals( Position p){
		return ( p.x == x && p.y == y);
	}

	public String toString(){
		return Integer.toString( x) + "," + Integer.toString( y);
	}
}