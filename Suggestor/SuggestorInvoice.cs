using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public interface SuggestorInvoice
    {
        string GetCustomerId();
        string GetInvoiceNo();
        Dictionary<string, SuggestorInvoiceLine> GetInvoiceLines();

        void SetCustomerId(string customerId);
        void SetInvoiceNo(string invoiceNo);
        void SetInvoiceLines(Dictionary<string, SuggestorInvoiceLine> invoiceLines);              
    }
}
