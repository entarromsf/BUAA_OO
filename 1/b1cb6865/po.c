#include <stdio.h>
#include <malloc.h>

struct poly{
	int coe;		//系数 
	int exp;		//次数 
	struct poly *next;
}; 

int main() {
	int c, n;
	int flag = 1;		// 1 = +, -1 = - 
	char s;
	struct poly *head = NULL, *p = NULL, *q;
	s = getchar();
	while (s != '\n') {
		if (s == '{') {
			s = getchar();
			if (s == '(') {
				scanf("%d,%d",&c,&n);
				//insert
				p = head;
				if (p == NULL) {
					q = (struct poly *)malloc(sizeof(struct poly));
					q->coe = c * flag;
					q->exp = n;
					q->next = NULL;
					head = q;
				}
				while (p != NULL) {
					if (n == p->exp) {
						p->coe = p->coe + flag * c;
						break;
					}
					else if (n < p->exp) {
						q = (struct poly *)malloc(sizeof(struct poly));
						q->coe = flag * c;
						q->exp = n;
						q->next = p;
						head = q;
						break;
					}
					else {
						if (p->next == NULL) {
							q = (struct poly *)malloc(sizeof(struct poly));
							q->coe = flag * c;
							q->exp = n;
							q->next = NULL;
							p->next = q;
							break;
						}
						else {
							if (n < p->next->exp) {
								q = (struct poly *)malloc(sizeof(struct poly));
								q->coe = flag * c;
								q->exp = n;
								q->next = p->next;
								p->next = q;
								break;
							}
							else
								p = p->next;
						}
					}
				}
				s = getchar();
			}
			else {
				printf("ERROR");
				return 0;
			}
		}
		else if (s == '+') {
			flag = 1;
			s = getchar();
		}
		else if (s == '-') {
			flag = -1;
			s = getchar();
		}
		else 
			s = getchar();
	}
	printf("{");
	p = head;
	while (p != NULL) {
		printf("(%d,%d)",p->coe,p->exp);
		if (p->next != NULL) 
			printf(",");
		p = p->next;
	}
	printf("}");
	return 0;
}
