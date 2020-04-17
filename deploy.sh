#!/bin/bash
shadow-cljs release :app &&
rsync -av target/ humapps:/var/www/pangrammer/
echo "Deployment process finished. Visit http://pangrammer.byu.edu to verify."
