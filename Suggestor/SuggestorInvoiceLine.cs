using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public interface SuggestorInvoiceLine
    {
        string GetItemNo();
        string GetDescription();
        double GetQuantity();
        double GetWeight();
        
        void SetItemNo(string itemNo);
        void SetDescription(string description);
        void SetQuantity(double quantity);
        void SetWeight(double weight);
    }
}
