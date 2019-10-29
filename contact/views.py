# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render, get_object_or_404, redirect
from .models import Contact
from .forms import ContactForm

# Create your views here.
def contact_list(request):
    template = 'contact_list.html'
    contacts = Contact.objects.all()

    context = {
        'contact_list' : contacts
    }
    return render(request, template, context)

def contact_details(request, pk):
    template = 'contact_details.html'
    contact = get_object_or_404(Contact, pk=pk)
    context = {
        'contact' : contact
    }
    return render(request, template, context)

def create(request):
    template = 'contact_form.html'

    if request.method == 'POST':
        form = ContactForm(request.POST)

        if form.is_valid():
            form.save()
            return redirect('contact_list')
    else:
        form = ContactForm();

    context = {
        'form' : form
    }
    return render(request, template, context)

def contact_edit(request, pk):
    template = 'contact_form.html'
    contact = get_object_or_404(Contact, pk=pk)
    form = ContactForm(request.POST or None, instance=contact)
    context = {
        'form' : form
    }

    if form.is_valid():
        form.save()
        return redirect('contact_list')
    return render(request, template, context)

def contact_delete(request, pk):
    template = 'contact_delete.html'
    contact = get_object_or_404(Contact, pk=pk)

    if request.method == 'POST':
        contact.delete()
        return redirect('contact_list')

    context = {
        'contact' : contact
    }
    return render(request, template, context)