#include<bits/stdc++.h>
using namespace std;
int ara[6]={95,4,1,8992,-3,0x114166};

void quick_sort(int l,int r)
{
    if(l>=r) return;
    int pivot_index=r;
    int i=l-1;
    for(int j=l;j<pivot_index;j++)
    {
        if(ara[j]<=ara[pivot_index])
        {
            i++;
            swap(ara[i],ara[j]);
        }
    }
    i++;
    swap(ara[i],ara[pivot_index]);

    quick_sort(l,i-1);
    quick_sort(i+1,r);


}

int main()
{
    int ara_left_index=0,ara_right_index=5;
    quick_sort(ara_left_index,ara_right_index);

    for(int i=ara_left_index;i<=ara_right_index;i++)
        cout<<ara[i]<<' ';
    return 0;
}
