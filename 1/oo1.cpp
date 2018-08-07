#include <stdio.h>
#include <stdlib.h>
typedef struct term
	{
		int c;
		int n;
	}Term;
/*
void selection_sort (Term A[],int n)
{
	int i, j, k;
	for (i = 1; i < n; i++)
	{
		k = i;
		for (j = i + 1; j < n; j++)
		{
			if (A[j].n < A[k].n) k = j;
		}
		if (k != i)
		{
			A[0] = A[i];
			A[i] = A[j];
			A[j] = A[0];
		}
	}
}
*/

int main ()
{
	char temp;
	int i,j,k;
	int num,nums = 1;
	int tag = 1;
	
	Term poly[1000] = {{0,0}};
	Term sum[1000] = {{0,0}};
	
	char op = '+';
	while ((temp = getchar()) == '{')
	{
		i = 0;
		do{
			scanf("(%d,%d)",&(poly[i].c),&(poly[i].n));
			i++;
		}while((temp = getchar()) == ',');
		num = i;
		if (op == '-')
		{
			for (i = 0; i < num ; i++)
			{
				poly[i].c = -poly[i].c;
			}
		}
		for (i = 0; i < num; i++)
		{
			tag = 1;
			for (j = 0; j < nums; j++)
			{
				if(poly[i].n == sum[j].n) 
				{
					sum[j].c = sum[j].c + poly[i].c;
					tag = 0;
					break;
				}
			}
			if (tag)
			{
				sum[nums].c = poly[i].c;
				sum[nums].n = poly[i].n;
				nums++;
			}
		}
		op = getchar();//'+'||'-'
		if(op == '\n') break;
	}
	printf("{");
	for (i = 0;i < nums; i++)
	{
		if(sum[i].c!=0)
		{
		    printf("(%d,%d)",sum[i].c,sum[i].n);
		    if(i<nums-1) printf(",");
	    }
	}
	printf("}");
	return 0;
}























