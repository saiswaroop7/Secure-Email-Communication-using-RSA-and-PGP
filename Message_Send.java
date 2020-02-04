
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class Prob3_Send {
    static int a,recpub;
    public static void main(String args[])
    {
        String subst;int i;
        String ct;
        int f[][] = new int[900][5];
        subst = "abcdefghijklmnopqrstuvwxyz ";
        char[] alphabet = subst.toCharArray();
        int[] alpha = new int[27];
        Random r = new Random();
        Scanner x = new Scanner(System.in).useDelimiter("\\n");
        System.out.println("Enter the receiver's public key: ");
        recpub = x.nextInt();
        int key[] = new int[4];
        //Alphabet Integer Value
        for(i=0;i<alpha.length;i++)
        {
            alpha[i] = i;
        }
        //Convert Key
        for(i=0;i<4;i++)
        {
            int n = r.nextInt(26);
            key[i] = alpha[n];
        }
        System.out.println("Key Generated: ");
        for(i=0;i<4;i++)
        {
            int n = key[i];
            System.out.print(alphabet[n]+" ");
        }
        //Alphabet Integer Value
        for(i=0;i<alpha.length;i++)
        {
            alpha[i] = i;
        }
        //Converting Encyrption Key to Single Integer.
        String temp="";
        for(i=0;i<4;i++)
        {
         String format = String.format("%02d", key[i]);
         temp +=format;
        }
        int session_key = Integer.parseInt(temp);
        ct = enprep(alpha,alphabet,key);
        System.out.println("Encrypted Text:");
        System.out.println(ct);
        int rsa = hashprep(ct, f, alphabet,alpha);
        //Encrypting session key using public key (B)
        BigInteger sesskey = RSA_B(session_key);
        //Using private key (A) and Signing Hash
        BigInteger sign = RSA_A(rsa);
        String to_send = "";
        //Send public key(A)//Sign//sesskey//Cipher Text
        to_send = a +"//"+sign+"//"+sesskey+"//"+ct;
        System.out.println("\nYou need to send below message to receiver:\n"+to_send);
    }
    public static String enprep(int[] alpha, char[] alphabet, int key[])
    {
        String pt,ctf = "";
        int i,j,k,count=0;
        Scanner x = new Scanner(System.in).useDelimiter("\\n");
        System.out.println("\nEnter the text you want to send: ");
        pt = x.nextLine();
        //pt = "In order to know why, first we need to know the ethical issues of how the machine learning algorithm is developed. the ethical issue does not actually relate to the way the algorithms are developed but it is more concerned with the way the data is being used. As most of us already know, there was recent scandal of with facebook. The Cambridge analytics used the data from FB without the user’s consent. This is big issue to keep in mind while deciding what kind of data should be used to develop an AI system. One another thing could be that sometimes an algorithm can make a decision and we wouldn’t know how or why it exactly made the decision. When a system diagnoses a person with cancer, sometimes we cannot know that why the system makes such a decision. A major issue in AI the racial bias. I have read the article Ain’t I a women which was posted on the discussion board, and it raises a major concern among the people. I had read another article where the facial recognition is being used for law enforcement to identify suspects via the surveillance cameras. If a system makes a wrong assumption about a person, then it could lead to a disaster. Of course, the decision of the algorithm is entirely unintentional. But it is still dangerous. But it is not all negatives, to be fair, we can think of an AI as a child, it is basically a computer program that learns and adapts as it is trained. It takes in huge amounts of data and is constantly learning from them. Therefore, it is up to us to train it in a way that benefits all of mankind because it has the potential to improve our lives profoundly. For example, AI can be used to help doctors diagnose cancer or prevent blindness. It can considerably reduce the risks taken by humans. AI is being extensively used in space exploration. There is a rover which known as Curiosity that roams the Mars collecting data. The use of AI is quite beneficial because there is no moral dilemma involved like saving a person or something. One of the main issues of AI is its ability to make a moral decision. For this, I have taken a test in the Moral Machine of MIT. There were like 10 questions in which we had decide a consequence A or B. It were mostly based on the Trolley problem. But it is these tests that are quite necessary for an AI system to improve and make better moral decisions.";
        char[] ptt = pt.toCharArray();
        i = (Math.round((ptt.length+5)/10)*10)+5;
        int[] ptf = new int[i];
        int ptf1[] = new int[ptf.length];
        int pf[]= new int[4];
        char ct[] = new char[pt.length()];
        //Convert Plaintext into Number.
        for(i=0;i<ptt.length;i++)
        {
            for(j=0;j<alphabet.length;j++)
            {  
                if(ptt[i]==alphabet[j]||ptt[i]==(alphabet[j]& 0x5f))
                {
                    ptf[i] = alpha[j];
                }
            }
            if(!((ptt[i] >= 'a' && ptt[i] <= 'z') || ptt[i]==' ' || (ptt[i] >= 'A' && ptt[i] <= 'Z')))
            {
                    ptf[i] = 26;   
            }
        }
        //Pass 4 blocks for encryption.
        for(i=0,k=0;i<ptf.length;i++)
        {
            if(k<ptt.length)
            {
            for(j=0;j<4;j++,k++)
            {
                pf[j] = ptf[k];
            }
            pf = encrypt(pf,key);
            for(int ij=0;ij<4;ij++,count++)
            {
                ptf1[count] = pf[ij];
            }}
        }
        //Convert Numeric Cipher to Alphabet
        for(i=0;i<ct.length;i++)
       {
           for(j=0;j<=26;j++)
           {
           if(ptf1[i]==alpha[j])
           {
             ct[i]=alphabet[j];
           }
           }
       }
        System.out.println("Given Plain Text: \n"+pt);
        for(i=0;i<ct.length;i++)
       {
           if(ct[i]==' ')
           {
               ctf = ctf+ct[i];
           }
           else{
               ctf = ctf+ct[i];
               }
       }
       return ctf;
    }
    public static int[] encrypt(int[] b,int[] key)
    {
        int i;
        int[] pf = new int[4];
        for(i=0;i<4;i++)
        {
            pf[i] = b[i]+key[i];
            pf[i] %= 27;
        }
        return pf;
    }
    
    public static int hashprep(String pt, int[][] f, char[] alphabet,int[] alpha)
    {
        int i,j,k;
        char[] ptt = pt.toCharArray();
        char[] ct = new char[5];
        int[] ptf =  new int[ptt.length];
        int pf[][] = new int[5][5];
        int[] OUT = new int[5];
        int[] FOUT = new int[5];
               for(i=0;i<ptt.length;i++)
        {
            for(j=0;j<alphabet.length;j++)
            {  
                if(ptt[i]==alphabet[j])
                {
                    ptf[i] = alpha[j];
        }}}
       //5 Bit String
       for(i=0,k=0;i<f.length;i++)
       {
           if(k>=ptf.length)
             {
                 break;
             }
         for(j=0;j<f[0].length;j++)
         {
             if(k<ptf.length)
             {
             f[i][j]=ptf[k];
             k++;
             }
             else
             {
                 break;
             }}}
       int cpos = k%5,rpos = i-1;
       //Padding
       if(cpos!=0)
        {
          for(j=cpos;j<f[0].length;j++)
           { 
             f[rpos][j] = 0;
           }
        }
       for(k=0;k<rpos+1;)
       {
           for(i=0;i<5;i++,k++)
           {
           for(j=0;j<5;j++)
           {
             pf[i][j]=f[k][j];
           }
           }
           OUT = hash(rpos,OUT,pf);
       }

       for(i=0;i<5;i++)
       {
            FOUT[i] += OUT[i];
            FOUT[i]%=27;
       }
       for(i=0;i<5;i++)
       {
           for(j=0;j<=26;j++)
           {
           if(FOUT[i]==alpha[j])
           {
             ct[i]=alphabet[j];
           }}}
       System.out.println("Output Hash:");
       for(i=0;i<5;i++)
       {
           System.out.print(ct[i]);
       }
       String temp="";
       for(i=0;i<4;i++)
       {
       String format = String.format("%02d", FOUT[i]);
       temp +=format;
       }
       int rsa = Integer.parseInt(temp);
       return rsa;
    }
    public static int[] hash(int rpos, int[] OUT,int[][] f)
    {
       int sum=0,i,j,k;
       //Round 1 OUT Result
       for(i=0;i<5;i++)
       {
           for(j=0;j<5;j++)
           {
               sum += f[j][i];
           }
           OUT[i]+=sum;
           OUT[i] = OUT[i]%27;
           sum=0;
       }
       int t1,t2,t3,t4;
       //Round 2 
       for(i=1,k=0;i<5;k++,i++)
       {
           if(i==1)
           {
            t1= f[k][0];
            for(j=0;j<5;j++)
            {
               if(j+1==5)
               {
                 f[k][j]=t1;
               }
               else
               {
                   f[k][j]=f[k][j+1];
               }}
           }
           if(i==2)
           {
               t1=f[k][0];
               t2=f[k][1];
            for(j=0;j<5;j++)
            {
               if(j+2==5)
               {
                 f[k][j]=t1;
               }
               else if(j+2==6)
               {
                 f[k][j]=t2;                                    
               }
               else
               {
               f[k][j]=f[k][j+2];                
               }}
           }
           if(i==3)
           {
               t1=f[k][0];
               t2=f[k][1];
               t3=f[k][2];
            for(j=0;j<5;j++)
            {
               if(j+3==5)
               {
                 f[k][j]=t1;
               }
               else if(j+3==6)
               {
                 f[k][j]=t2;                                    
               }
               else if(j+3==7)
               {
                 f[k][j]=t3;                                    
               }
               else
               {
               f[k][j]=f[k][j+3];
               }}
           }
           if(i==4)
           {
               t1=f[k][0];
               t2=f[k][1];
               t3=f[k][2];
               t4=f[k][3];
            for(j=0;j<5;j++)
            {
               if(j+4==5)
               {
                 f[k][j]=t1;
               }
               else if(j+4==6)
               {
                 f[k][j]=t2;                                    
               }
               else if(j+4==7)
               {
                 f[k][j]=t3;                                    
               }
               else if(j+4==8)
               {
                 f[k][j]=t4;                                    
               }
               else
               {
               f[k][j]=f[k][j+4];
               }}
       }}
       //Round 2 OUT Result
       for(i=0;i<5;i++)
       {
           for(j=0;j<5;j++)
           {
               sum += f[j][i];
           }
           sum = sum%27;
           OUT[i] +=sum;
           OUT[i] = OUT[i]%27;
           sum=0;
       }
       //Round 3
       for(i=1,k=0;i<=5;k++,i++)
       {
           if(i==4)
           {
            t1= f[0][k];
            for(j=0;j<5;j++)
            {
               if(j+1==5)
               {
                 f[j][k]=t1;
               }
               else
               {
                   f[j][k]=f[j+1][k];
               }}
           }
           if(i==3)
           {             
               t1=f[0][k];
               t2=f[1][k];
            for(j=0;j<5;j++)
            {
               if(j+2==5)
               {
                 f[j][k]=t1;
               }
               else if(j+2==6)
               {
                 f[j][k]=t2;                                    
               }
               else
               {
               f[j][k]=f[j+2][k];                
               }}
           }
           if(i==2)
           {
               t1=f[0][k];
               t2=f[1][k];
               t3=f[2][k];
            for(j=0;j<5;j++)
            {
               if(j+3==5)
               {
                 f[j][k]=t1;
               }
               else if(j+3==6)
               {
                 f[j][k]=t2;                                    
               }
               else if(j+3==7)
               {
                 f[j][k]=t3;                                    
               }
               else
               {
               f[j][k]=f[j+3][k];
               }}
           }
           if(i==1)
           {
               t1=f[0][k];
               t2=f[1][k];
               t3=f[2][k];
               t4=f[3][k];
            for(j=0;j<5;j++)
            {
               if(j+4==5)
               {
                 f[j][k]=t1;
               }
               else if(j+4==6)
               {
                 f[j][k]=t2;                                    
               }
               else if(j+4==7)
               {
                 f[j][k]=t3;                                    
               }
               else if(j+4==8)
               {
                 f[j][k]=t4;                                    
               }
               else
               {
               f[j][k]=f[j+4][k];
               }}
       }}
       //Round 3 OUT Result
       for(i=0;i<5;i++)
       {
           for(j=0;j<5;j++)
           {
               sum += f[i][j];
           }
           sum = sum%27;
           OUT[i] +=sum;
           OUT[i] = OUT[i]%27;
           sum=0;
       }

       return OUT;
     }
    public static BigInteger RSA_A(int ct)
    {
             BigInteger d,n,phi,ctf;
             double k=0;
             int p=78511,q=5657;
             //Calculating O(n)
             phi = BigInteger.valueOf((p-1)*(q-1));
             //Public Key Value.
             n = BigInteger.valueOf(p*q);
             
             //Public Key Me = 101 (Start);
             //Public Key Jai = 2618;
             //Calcualting a value
             for(a=101;a<phi.intValue();a++)
             {
                 if(gcd(a,phi)==1)
                 {
                     break;
                 }
             }
            //Private Key (Extended Euclidean)
            while(true)
             {
              if((1 + (k*phi.intValue()))%a==0)
              {
               d = BigInteger.valueOf((int) ((1 + (k*phi.doubleValue()))/a));
               break;
              }
              k++;
            }
            BigInteger ecc;
            //Signing 5 bit Hash Digest
            ctf = BigInteger.valueOf(ct);
            ecc = square(ctf,d,n);
            System.out.println("\nSigning the hash digest using sender's private key...");
            System.out.println("Signed Hash Digest: ");
            System.out.print(ecc);
            return ecc;
    }        
    
    public static BigInteger RSA_B(int pt)
    {
            BigInteger n,ptf,ee;
            int p=78511,q=5657;
            //Public Key Value.
            n = BigInteger.valueOf(p*q);
            BigInteger ecc;
            ee = BigInteger.valueOf((int)recpub);
            //Encrypted Data.
            ptf = BigInteger.valueOf(pt);
            ecc = square(ptf,ee,n);
            System.out.println("\nUsing Receiver's public key to encrypt Session Key...");
            System.out.println("Encrypted Session Key: ");
            System.out.print(ecc);
            return ecc;
    }        
    public static double gcd(double e,BigInteger phi)
    {
        BigInteger temp;
        while(phi.intValue()!=0)
        {
            temp = BigDecimal.valueOf(e).toBigInteger();
            e = phi.intValue();
            phi = temp.mod(phi);
        }
        return e;
    }
    public static BigInteger square(BigInteger ptf,BigInteger pow,BigInteger n)
    {
        int p=1,temp=0,power=0,j=0;
        BigInteger product,tmp,decrypt;
        product = BigDecimal.valueOf(p).toBigInteger();
        //Binary 
        int[] a = new int[100];
        String qe = Integer.toBinaryString(pow.intValue());
        char[] q = qe.toCharArray();
        for(int i=q.length-1;i>=0;i--)
        {
             temp = Character.getNumericValue(q[i]);
             if(temp==1)
             {
             a[j] = (int)Math.pow(2, power);
             j++;power++;
             }
             else
             {
               power++; 
             }
        }
        //Square and Multiply
        for(int ij =0; ij<a.length;ij++)
        {
        if(a[ij]!=0)    
        {
           tmp = BigDecimal.valueOf(p).toBigInteger();
           for(int i=0;i<=a[ij];i+=2)
                {
                    if(i==a[ij])
                    {
                        tmp = tmp.mod(n);
                        product = product.multiply(tmp);
                        break;
                    }
                    if(a[ij]!=1)
                    {
                       decrypt = ptf;
                       decrypt = decrypt.pow(2);
                       decrypt = decrypt.mod(n);
                       tmp = tmp.multiply(decrypt);
                       tmp = tmp.mod(n);
                    }
                    else
                    {
                        decrypt = ptf.pow(1);
                        decrypt = decrypt.mod(n);
                        product = product.multiply(decrypt);
                    }
                }}}
       product = product.mod(n);
       return product;
    }
}