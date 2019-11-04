#include<bits/stdc++.h>

using namespace std;
#define size1 1000
//int ara[size1];

int* bubble(int ara[],int n)
{
    for(int i=0;i<n;i++)
    {
        for(int j=0,c=0;j<n-1;j++)
        {
            if(ara[j]>ara[j+1]) {swap(ara[j],ara[j+1]);
                //int temp=ara[j];
                //ara[j]=ara[j+1];
                //ara[j+1]=temp;
            }
        }
    }

return ara;

}

int main()
{
    int ara[100],n;
    cin>>n;
    for(int i=0;i<n;i++)cin>>ara[i];
    bubble(ara,n);
    for(int i=0;i<n;i++)
    cout<<ara[i]<<' ';
}
