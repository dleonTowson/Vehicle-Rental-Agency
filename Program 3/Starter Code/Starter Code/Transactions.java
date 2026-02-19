public class Transactions 
{

    private static class TransactionNode 
    {
        Transaction data;
        TransactionNode next;

        TransactionNode(Transaction data) 
        {
            this.data = data;
            this.next = null;
        }
    }

    private TransactionNode head;
    private TransactionNode iterCurrent;

    public Transactions() 
    {
        head = null;
        iterCurrent = null;
    }

    public void add(Transaction tran) 
    {
        TransactionNode node = new TransactionNode(tran);
        // insert at front for easy
        node.next = head;
        head = node;
    }

    public void reset() 
    {
        iterCurrent = head;
    }

    public boolean hasNext() 
    {
        return iterCurrent != null;
    }

    public Transaction getNext() 
    {
        if (!hasNext()) 
        {
            return null;
        }
        Transaction temp = iterCurrent.data;
        iterCurrent = iterCurrent.next;
        return temp;
    }
}