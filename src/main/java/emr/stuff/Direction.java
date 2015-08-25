package emr.stuff;

public enum Direction
{
	N   ( 0 , -1 ) ,
    NE  ( 1 , -1 ) ,
    E   ( 1 , 0 ) ,
    SE  ( 1 , 1 ) ,
    S   ( 0 , 1 ) ,
    SW  ( -1 , 1 ) ,
    W   ( -1 , 0 ) ,
    NW  ( -1 , -1 ) ,
    NO_DIRECTION ( 0 , 0 );
	
	public final int X, Y;
	
	Direction( int x , int y )
	{
		X = x;
		Y = y;
	}
	
	public Direction getNextClockwiseDirection()
	{
		Direction next;
		switch( this )
		{
			case N:
				next = NE;
				break;
			case NE:
				next = E;
				break;
			case E:
				next = SE;
				break;
			case SE:
				next = S;
				break;
			case S:
				next = SW;
				break;
			case SW:
				next = W;
				break;
			case W:
				next = NW;
				break;
			case NW:
				next = N;
				break;
			default:
				next = NO_DIRECTION;
		}
		return next;
	}
	
	public Direction getNextCounterClockwiseDirection()
	{
		Direction next;
		switch( this )
		{
			case N:
				next = NW;
				break;
			case NE:
				next = N;
				break;
			case E:
				next = NE;
				break;
			case SE:
				next = E;
				break;
			case S:
				next = SE;
				break;
			case SW:
				next = S;
				break;
			case W:
				next = SW;
				break;
			case NW:
				next = W;
				break;
			default:
				next = NO_DIRECTION;
		}
		return next;
	}
	
	public static Direction getDirectionByValue( int x , int y )
	{
		Direction dir = Direction.NO_DIRECTION; //if x or y is a bad number you just get this one
		if( x > 0 )
        {
            if( y < 0 ) dir = Direction.NE;
            if( y == 0 ) dir = Direction.E;
            if( y > 0 ) dir = Direction.SE;
        }
        if( x < 0 )
        {
            if( y < 0 ) dir = Direction.NW;
            if( y == 0 ) dir = Direction.W;
            if( y > 0 ) dir = Direction.SW;
        }
        if( x == 0 )
        {
            if( y < 0 ) dir = Direction.N;
            if( y > 0 ) dir = Direction.S;
        }
		return dir;
	}
}